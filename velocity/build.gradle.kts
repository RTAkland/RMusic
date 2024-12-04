import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("xyz.jpenilla.run-velocity") version "2.3.1"
    id("com.gradleup.shadow") version "8.3.0"
}

val modVersion: String by project

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}


tasks.processResources {
    val properties = mapOf(
        "version" to modVersion
    )
    inputs.properties(properties)
    filteringCharset = "UTF-8"
    filesMatching("velocity-plugin.json") {
        expand(properties)
    }
}

tasks.runVelocity {
    velocityVersion("3.4.0-SNAPSHOT")
    systemProperty("file.encoding", "UTF-8")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    exclude("com/google/gson/**")
    exclude("org/jetbrains/**")
    exclude("org/intellij/**")
    exclude("org/slf4j/**")
}

tasks.jar {
    enabled = false
}

tasks.compileJava {
    options.encoding = "UTF-8"
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

tasks.compileKotlin {
    compilerOptions.jvmTarget = JvmTarget.JVM_17
}