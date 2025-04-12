import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
            // TODO: set -Xjdk-release. See KT-49746
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        binaries {
            executable {
                mainClass = "tech.pacia.okapi.cli.MainKt"
            }
        }
    }

    macosX64()
    macosArm64()

    linuxX64()
    // linuxArm64() // not supported by okapi-client

    // mingwX64() // not supported by okapi-client

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.clikt)
                implementation(libs.kotlinx.serialization.json)
                implementation(projects.okapiClient)
            }
        }

        macosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        targets.withType<KotlinNativeTarget> {
            binaries {
                executable {
                    baseName = "okapi"
                    entryPoint = "tech.pacia.okapi.cli.main"
                }
            }
        }
    }
}
