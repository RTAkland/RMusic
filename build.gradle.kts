plugins {
    kotlin("jvm") version "2.0.21"
    id("fabric-loom") version "1.8-SNAPSHOT"
    id("com.gradleup.shadow") version "8.3.0"
}

val minecraftVersion: String by project
val yarnMappings: String by project
val modVersion: String by project
val loaderVersion: String by project
val fabricLanguageKotlinVersion: String by project
val fabricVersion: String by project

version = modVersion
group = "cn.rtast"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricLanguageKotlinVersion")
    implementation("org.jflac:jflac-codec:1.5.2")
    implementation("com.github.goxr3plus:java-stream-player:10.0.2")
    implementation("com.googlecode.soundlibs:soundlibs:1.4")
    implementation("com.google.zxing:core:3.5.2")
    implementation("com.google.zxing:javase:3.5.2")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

tasks.processResources {
    filteringCharset = "UTF-8"
    filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "version" to project.version,
                "minecraft_version" to minecraftVersion,
                "yarn_mappings" to yarnMappings,
                "fabric_language_kotlin_version" to fabricLanguageKotlinVersion,
                "loader_version" to loaderVersion
            )
        )
    }
}

tasks.compileJava {
    options.encoding = "UTF-8"
    options.release.set(21)
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

tasks.jar {
    from("LICENSE") {
        rename { "LICENSE-RMusic" }
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    dependencies {
        include(dependency("com.squareup.okhttp3:okhttp:5.0.0-alpha.11"))
        include(dependency("com.github.goxr3plus:java-stream-player:10.0.2"))
        include(dependency("com.google.zxing:core:3.5.2"))
        include(dependency("com.google.zxing:javase:3.5.2"))
        include(dependency("com.googlecode.soundlibs:soundlibs:1.4"))
        include(dependency("org.jflac:jflac-codec:1.5.2"))
        include(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0"))
    }
}