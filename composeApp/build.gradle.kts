import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.detekt)
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

val key = properties["okapi.consumerKey"] as? String ?: "NOT_PROVIDED"
val secret = properties["okapi.consumerSecret"] as? String ?: "NOT_PROVIDED"

val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("keystore.properties")
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

buildConfig {
    buildConfigField("String", "CONSUMER_KEY", "\"$key\"")
    buildConfigField("String", "CONSUMER_SECRET", "\"$secret\"")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }

    jvm("desktop")

    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(compose.materialIconsExtended)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)

                implementation(libs.navigation.compose)
                implementation(libs.lifecycle.viewmodel.compose)

                implementation(libs.landscapist)

                // detektPlugins(libs.detekt.formatting)
                // detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")

                implementation(project(":okapi-client"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)

                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
            }
        }

        androidMain {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)

                implementation(libs.ktor.client.okhttp)
                implementation(libs.maps.compose)
                implementation(libs.play.services.maps)
            }
        }

        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "tech.pacia.opencaching"
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "tech.pacia.opencaching"
        minSdk = 24
        targetSdk = 35
        versionCode = (findProperty("versionCode") as? String)?.toIntOrNull() ?: 1
        versionName = findProperty("versionName") as? String ?: "1.0.0"

        buildConfigField(
            type = "String",
            name = "OKAPI_CONSUMER_KEY",
            value = "\"${key}\"",
        )

        buildConfigField(
            type = "String",
            name = "OKAPI_CONSUMER_SECRET",
            value = "\"${secret}\"",
        )
    }

    signingConfigs {
        create("release") {
            storeFile = if (keystorePropertiesFile.exists()) file(keystoreProperties["storeFile"] as String) else null
            storePassword = if (keystorePropertiesFile.exists()) keystoreProperties["storePassword"] as String else null
            keyAlias = if (keystorePropertiesFile.exists()) keystoreProperties["keyAlias"] as String else null
            keyPassword = if (keystorePropertiesFile.exists()) keystoreProperties["keyPassword"] as String else null
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        named("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isShrinkResources = false
            isMinifyEnabled = false
        }

        named("release") {
            signingConfig = signingConfigs.getByName("release")
            isShrinkResources = true
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    dependencies {
        debugImplementation(compose.uiTooling)
        detektPlugins(libs.detekt.formatting)
    }
}

compose.desktop {
    application {
        mainClass = "tech.pacia.opencaching.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "tech.pacia.opencaching"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<Detekt> {
    buildUponDefaultConfig = true
    autoCorrect = true
    jvmTarget = "11"
    exclude {
        // See https://github.com/detekt/detekt/issues/5611#issuecomment-1364313507
        it.file.relativeTo(projectDir).startsWith("build")
    }
}
