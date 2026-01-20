import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.detekt)
}

kotlin {
    androidLibrary {
        minSdk = 21
        namespace = "compose.project.demo.composedemo"
        compileSdk = 36

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }

        androidResources {
            enable = true
        }
    }

    jvm("desktop")

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.runtime)
                implementation(libs.foundation)
                implementation(libs.material3)
                implementation(libs.ui)
                implementation(libs.components.resources)
                implementation(libs.ui.tooling.preview)

                implementation(libs.material.icons.extended)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)

                implementation(libs.androidx.navigation.compose)
                implementation(libs.androidx.viewmodel.compose)

                implementation(libs.landscapist)

                // detektPlugins(libs.detekt.formatting)
                // detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")

                implementation(projects.okapiClient)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.ui.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ui.tooling.preview)
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

//dependencies {
//    debugImplementation(compose.uiTooling)
//    detektPlugins(libs.detekt.formatting)
//}