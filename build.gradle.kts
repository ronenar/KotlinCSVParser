import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.dokka") version ("1.4.20")
    application
}
group = "me.ronen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/dokka/dev")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("org.jetbrains.dokka:kotlin-as-java-plugin:1.4.20")
}

tasks.withType<KotlinCompile> (){
    kotlinOptions.jvmTarget = "1.8"
}
application {
    mainClassName = "MainKt"
}