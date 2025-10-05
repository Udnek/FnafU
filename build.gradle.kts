import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.1.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
}

group = "me.udnek"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    // Paper API
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")

    // ProtocolLib
    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")

    // SkinsRestorer API
    compileOnly("net.skinsrestorer:skinsrestorer-api:15.7.6")

    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))

    // Системные зависимости из pom.xml (CoreU и JeiU)
    compileOnly(files("C:/Users/glebd/OneDrive/Documents/CODING/Java/CoreU/build/libs/CoreU-1.0-SNAPSHOT.jar"))
    compileOnly(files("C:/Users/glebd/OneDrive/Documents/CODING/Java/JeiU/out/artifacts/JeiU.jar"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }
    compileJava {
        options.release.set(21)
    }
    shadowJar {
        archiveBaseName.set("FnafU")
        archiveClassifier.set("")
        archiveVersion.set(version.toString())
    }
}