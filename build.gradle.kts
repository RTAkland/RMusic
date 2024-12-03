plugins {
    kotlin("jvm") version "2.0.21"
}

val modVersion: String by project

allprojects {
    version = modVersion
    group = "cn.rtast"

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    apply {
        apply(plugin = "org.jetbrains.kotlin.jvm")
    }

    tasks.jar {
        from("LICENSE") {
            rename { "ROneBot-LICENSE-Apache2.0" }
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    }
}
