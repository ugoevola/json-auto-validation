plugins {
    kotlin("jvm") version "1.8.21"
    `java-library`
    `maven-publish`
    id("signing")
}

group = "io.github.ugoevola"
version = "0.0.1"

val springBootVersion = "3.2.1"
val ktlVersion = "1.9.22"
val jsonVersion = "20231013"
val jsonSchemaValidatorVersion = "1.1.0"
val byteBuddyVersion = "1.14.11"
val kLoggingVersion = "3.0.5"
val reflectionVersion = "0.10.2"

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
    compileOnly("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
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
    withJavadocJar()
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
        register("mavenJava", MavenPublication::class) {
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()
            from(components["java"])

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
    }

    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.properties["username"] as String
                password = project.properties["password"] as String
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

tasks {
    named<Javadoc>("javadoc") {
        if (JavaVersion.current().isJava9Compatible) {
            options {
                this as StandardJavadocDocletOptions
                addBooleanOption("html5", true)
            }
        }
    }
}
