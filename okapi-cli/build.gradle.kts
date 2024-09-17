import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

//tasks.withType<Jar> {
//    manifest {
//        attributes["Main-Class"] = "tech.pacia.okapi.cli.AppKt"
//    }
//}

kotlin {
    jvm()

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
                implementation(project(":okapi-client"))
            }
        }

        macosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        // TODO: Make JVM build work
//        jvmMain {
//            dependencies {
//                implementation(libs.clikt)
//                implementation(project(":okapi-client"))
//            }
//        }
//
//        targets.withType<KotlinJvmTarget> {
//            @OptIn(ExperimentalKotlinGradlePluginApi::class)
//            mainRun {
//                mainClass = "tech.pacia.okapi.cli.MainKt"
//            }
//        }

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
