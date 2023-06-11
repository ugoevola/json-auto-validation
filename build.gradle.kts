plugins {
    kotlin("jvm") version "1.8.21"
    `java-library`
    `maven-publish`
}

group = "org.uevola"
version = "0.0.1-SNAPSHOT"

val springVersion = "3.0.7"
val ktlVersion = "1.8.21"
val jsonVersion = "20230227"
val jsonSchemaValidatorVersion = "1.0.83"
val byteBuddyVersion = "1.14.5"
val kLoggingVersion = "3.0.5"
val reflectionVersion = "0.10.2"

repositories {
    mavenCentral()
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib", ktlVersion))
    implementation(kotlin("reflect", ktlVersion))
    // spring
    compileOnly("org.springframework.boot:spring-boot-starter-web:$springVersion")
    // json manipulation
    api("org.json:json:$jsonVersion")
    // validation
    api("com.networknt:json-schema-validator:$jsonSchemaValidatorVersion")
    implementation("net.bytebuddy:byte-buddy:$byteBuddyVersion")
    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:$kLoggingVersion")
    // lecture annotation
    implementation("org.reflections:reflections:$reflectionVersion")
}

kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}
