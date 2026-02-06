plugins {
    `java-library`
    kotlin("jvm") version "2.3.0"
    id("com.vanniktech.maven.publish") version "0.34.0"
    id("signing")
    id("org.jetbrains.dokka") version "2.1.0"
    kotlin("plugin.spring") version "2.3.0"
}

group = "io.github.ugoevola"
version = "1.0.1"

val springBootVersion = "4.0.1"
val ktlVersion = "2.3.0"
val jsonVersion = "20231013"
val jsonSchemaValidatorVersion = "3.0.0"
val byteBuddyVersion = "1.14.11"
val kLoggingVersion = "7.0.13"
val javapoetVersion = "1.13.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib", ktlVersion))
    implementation(kotlin("reflect", ktlVersion))
    // spring
    compileOnly("org.springframework.boot:spring-boot-starter-webmvc:$springBootVersion")
    compileOnly("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    implementation ("org.springframework.boot:spring-boot-starter-json:$springBootVersion")
    // validation
    api("com.networknt:json-schema-validator:$jsonSchemaValidatorVersion")
    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:$kLoggingVersion")
    // bean generation
    implementation("com.squareup:javapoet:$javapoetVersion")
}

kotlin {
    jvmToolchain(25)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(group.toString(), rootProject.name, version.toString())

    pom {
        name.set("Json Auto Validation")
        description.set("Json-auto-validation is a library for automatic validation of incoming json data in a spring-boot API.")
        url.set("https://github.com/ugoevola/json-auto-validation")
        inceptionYear.set("2023")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("ugoevola")
                name.set("Ugo Evola")
                email.set("ugoevol@laposte.net")
            }
        }
        scm {
            connection.set("scm:git:git:github.com/ugoevola/json-auto-validation.git")
            developerConnection.set("scm:git:ssh://github.com/ugoevola/json-auto-validation.git")
            url.set("https://github.com/ugoevola/json-auto-validation")
        }
    }
}
