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
        create(IntelliJPlatformType.Gateway, "2024.3")
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

val payloadPluginZip: Configuration by configurations.creating {
    isTransitive = false
    attributes {
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements::class.java, "plugin-zip"))
    }
}

dependencies {
    payloadPluginZip(project(":payload-plugin"))
}

tasks.prepareSandbox {
    into(pluginName.map { "$it/resources/payloadPlugin" }) {
        from(payloadPluginZip)
    }
}