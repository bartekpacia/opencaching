import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.buildConfig)
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

android {
    namespace = "tech.pacia.opencaching"
    compileSdk = 36

    defaultConfig {
        applicationId = "tech.pacia.opencaching"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ui.tooling.preview)

    implementation(libs.ktor.client.okhttp)
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
}
