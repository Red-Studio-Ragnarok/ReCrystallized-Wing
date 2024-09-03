import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.Gradle

plugins {
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    id("org.parchmentmc.librarian.forgegradle") version "1.+"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
    id("com.github.gmazzo.buildconfig") version "5.4.0"
    id("io.freefair.lombok") version "8.7.1"
}

group = "dev.redstudio"
version = "2.0" // Versioning must follow the Ragnarök versioning convention: https://github.com/Red-Studio-Ragnarok/Commons/blob/main/Ragnar%C3%B6k%20Versioning%20Convention.md

val id = "rcw"
val minecraftVersion = "1.20.1"

minecraft {
    mappings("parchment", "2023.09.03-$minecraftVersion")

    copyIdeResources.set(true)

    runs {
        create("client") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            mods {
                create(id) {
                    source(sourceSets.main.get())
                }
            }
        }
        create("server") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            mods {
                create(id) {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

sourceSets.main {
    resources {
        srcDir("src/generated/resources")
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:$minecraftVersion-47.3.7")
}

buildConfig {
    packageName("${project.group}.${id}")
    className("ProjectConstants")
    documentation.set("This class defines constants for ${project.name}.\n<p>\nThey are automatically updated by Gradle.")

    useJavaOutput()
    buildConfigField("String", "ID", provider { """"$id"""" })
    buildConfigField("String", "NAME", provider { """"${project.name}"""" })
    buildConfigField("String", "VERSION", provider { """"${project.version}"""" })
    buildConfigField("org.apache.logging.log4j.Logger", "LOGGER", "org.apache.logging.log4j.LogManager.getLogger(NAME)")
}

// Set the toolchain version to decouple the Java we run Gradle with from the Java used to compile and run the mod
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
    withSourcesJar() // Generate sources jar
}

tasks {
    processResources {
        val expandProperties = mapOf(
            "version" to project.version,
            "name" to project.name,
            "id" to id
        )

        inputs.properties(expandProperties)

        filesMatching("**/*.*") {
            if (!path.endsWith(".png"))
                expand(expandProperties)
        }
    }

    jar {
        finalizedBy("reobfJar")
    }

    withType<Jar>().configureEach {
        archiveBaseName.set(archiveBaseName.get().replace(" ", "-") + "-[$minecraftVersion]")
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.isFork = true
        options.forkOptions.jvmArgs = listOf("-Xmx4G")
    }
}

idea {
    module {
        inheritOutputDirs = true
        excludeDirs.addAll(setOf(".github", ".gradle", ".idea", "build", "gradle", "run").map(::file))
    }

    project {
        settings {
            jdkName = "17"
            languageLevel = IdeaLanguageLevel("JDK_17")

            runConfigurations {
                listOf("Client", "Server", "Obfuscated Client", "Obfuscated Server", "Vanilla Client", "Vanilla Server").forEach { name ->
                    create(name, Gradle::class.java) {
                        val prefix = name.substringBefore(" ").let { if (it == "Obfuscated") "Obf" else it }
                        val suffix = name.substringAfter(" ").takeIf { it != prefix } ?: ""
                        taskNames = setOf("run$prefix$suffix")
                    }
                }
            }
        }
    }
}
