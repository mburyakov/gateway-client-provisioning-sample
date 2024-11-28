import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.1.0"
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create(IntelliJPlatformType.IntellijIdeaCommunity, "2024.3")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "243.21565"
        }
        version = project.version.toString()
    }
    buildSearchableOptions = false
    instrumentCode = false
}

val pluginZip: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false

    attributes {
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named("plugin-zip"))
    }
}

artifacts {
    add(pluginZip.name, tasks.buildPlugin)
}