rootProject.name = "Re-Crystallized Wing"

pluginManagement {
    repositories {
        maven {
            // ModDevGradle
            name = "NeoForged Maven"
            url = uri("https://maven.neoforged.net/releases/")
            content {
                includeGroup("net.neoforged")
            }
        }
        maven {
            name = "Parchment Maven"
            url = uri("https://maven.parchmentmc.org/")
        }
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0" // Automatic toolchain provisioning
}
