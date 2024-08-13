import org.jetbrains.gradle.ext.settings
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.runConfigurations
import org.gradle.api.tasks.bundling.Zip

plugins {
    id("com.gtnewhorizons.retrofuturagradle") version "1.4.1"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
    id("com.github.gmazzo.buildconfig") version "5.4.0"
    id("io.freefair.lombok") version "8.6"
}

group = "dev.redstudio"
version = "2.0" // Versioning must follow Ragnarök versioning convention: https://github.com/Red-Studio-Ragnarok/Commons/blob/main/Ragnar%C3%B6k%20Versioning%20Convention.md

val id = "rcw"

minecraft {
    mcVersion = "1.12.2"
    username = "Desoroxxx"
    extraRunJvmArguments = listOf("-Dforge.logging.console.level=debug")
}

buildConfig {
    packageName("${project.group}.${id}")
    className("ProjectConstants")
    documentation.set("This class defines constants for ${project.name}.\n<p>\nThey are automatically updated by Gradle.")

    useJavaOutput()
    buildConfigField("String", "ID", provider { """"${id}"""" })
    buildConfigField("String", "NAME", provider { """"${project.name}"""" })
    buildConfigField("String", "VERSION", provider { """"${project.version}"""" })
    buildConfigField("org.apache.logging.log4j.Logger", "LOGGER", "org.apache.logging.log4j.LogManager.getLogger(NAME)")
}

idea {
    module {
        inheritOutputDirs = true

        excludeDirs = setOf(
                file(".github"), file(".gradle"), file(".idea"), file("build"), file("gradle"), file("run")
        )
    }

    project {
        settings {
            jdkName = "1.8"
            languageLevel = IdeaLanguageLevel("JDK_1_8")

            runConfigurations {
                create("Client", Gradle::class.java) {
                    taskNames = setOf("runClient")
                }
                create("Server", Gradle::class.java) {
                    taskNames = setOf("runServer")
                }
                create("Obfuscated Client", Gradle::class.java) {
                    taskNames = setOf("runObfClient")
                }
                create("Obfuscated Server", Gradle::class.java) {
                    taskNames = setOf("runObfServer")
                }
                create("Vanilla Client", Gradle::class.java) {
                    taskNames = setOf("runVanillaClient")
                }
                create("Vanilla Server", Gradle::class.java) {
                    taskNames = setOf("runVanillaServer")
                }
            }
        }
    }
}

// Set the toolchain version to decouple the Java we run Gradle with from the Java used to compile and run the mod
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
    withSourcesJar() // Generate sources jar when building and publishing
}

tasks.processResources.configure {
    inputs.property("version", project.version)
    inputs.property("name", project.name)
    inputs.property("id", id)

    filesMatching("**/*.*") {
        if (!file.absolutePath.contains("png")) {
            expand(mapOf("version" to project.version, "name" to project.name, "id" to id))
        }
    }
}

tasks.register<Zip>("packageResourcePacks") {
    group = "build"
    description = "Packs resourcepacks into zip files and places them in the build/libs directory."
    destinationDirectory.set(layout.buildDirectory.dir("libs"))

    // Include the *processed resources* in the ZIP
    val resourcePacksDir =layout.buildDirectory.dir("resources/main/resourcepacks").get().asFile
    resourcePacksDir.listFiles()?.filter { it.isDirectory }?.forEach { dir ->
        from(dir)

        // Transform the folder name, replace underscores and capitalize words
        val resourcePackName = dir.name
            .replace("_", " ")
            .split(" ")
            .joinToString(" ") { it -> it.replaceFirstChar { it.uppercase() } }

        archiveFileName.set("${project.name} $resourcePackName ${project.version}.zip")
    }
}

tasks.named("processResources") {
    finalizedBy("packageResourcePacks")
}

tasks.withType<Jar>().configureEach {
    archiveBaseName.set(archiveBaseName.get().replace(" ", "-"))
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.isFork = true
    options.forkOptions.jvmArgs = listOf("-Xmx4G")
}
