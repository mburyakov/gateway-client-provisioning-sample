import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.utils.asPath

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
    }
    buildSearchableOptions = false
    instrumentCode = false
}

val myDownloads by configurations.creating {
    isTransitive = false
    attributes {
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements::class.java, "plugin-zip"))
    }
}

dependencies {
    myDownloads(project(":payload-plugin"))
}

tasks.prepareSandbox {
    into(pluginName.map { "$it/resources/payloadPlugin" }) {
        from(myDownloads)
    }
}