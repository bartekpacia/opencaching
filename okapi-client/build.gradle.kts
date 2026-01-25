import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidMultiplatformLibrary)
}

description = "Client for OKAPI"

kotlin {
    explicitApi()

    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
            // TODO: set -Xjdk-release
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    macosX64()
    macosArm64()

    linuxX64()
    linuxArm64()

    // mingwX64() // not supported by ktor-client-cio

    android {
        namespace = "tech.pacia.okapi.client"
        compileSdk = 36

        // publishLibraryVariants("release")
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.ktor.client.core)
                // implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlincrypto.hmac.sha1)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}
