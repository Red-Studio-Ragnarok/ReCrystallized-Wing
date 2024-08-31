rootProject.name = "Re-Crystallized Wing"

pluginManagement {
    repositories {
        maven {
            // ForgeGradle
            name = "Minecraft Forge Maven"
            url = uri("https://maven.minecraftforge.net/")
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
