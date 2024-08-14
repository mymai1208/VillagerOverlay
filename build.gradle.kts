plugins {
    java
    kotlin("jvm") version "1.9.23"
    id("fabric-loom") version "1.7-SNAPSHOT"
}

val mod_version: String by project

group = "net.mymai1208"
version = mod_version

val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val fabric_kotlin_version: String by project

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings("net.fabricmc:yarn:${yarn_mappings}")

    modImplementation("net.fabricmc:fabric-loader:${loader_version}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${fabric_kotlin_version}")
}

tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand(mapOf(
                "version" to mod_version,
                "fabric_kotlin_version" to fabric_kotlin_version,
                "fabric_loader_version" to loader_version
            ))
        }
    }

    test {
        useJUnitPlatform()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}