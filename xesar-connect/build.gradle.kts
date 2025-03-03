/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/8.1.1/userguide/building_java_projects.html
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("com.diffplug.spotless") version "7.0.2"
    kotlin("plugin.serialization") version "2.1.10"
    id("com.adarshr.test-logger") version "4.0.0"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `maven-publish`
    signing
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

val ktorVersion: String = "3.1.1"
val kotlinxVersion: String = "1.10.1"
val kotlinLoggingVersion: String = "3.0.5"
val kotestVersion: String = "5.9.1"
val logbackVersion: String = "1.5.17"
val pahoVersion: String = "1.2.5"
val mockkVersion: String = "1.13.17"
val kotestTestcontainersVersion: String = "2.0.2"

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    testImplementation(kotlin("test"))

    // This dependency is used internally, and not exposed to consumers on their own compile
    // classpath.

    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:$pahoVersion")

    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation("io.ktor:ktor-serialization-kotlinx:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation(
        "io.kotest.extensions:kotest-extensions-testcontainers:$kotestTestcontainersVersion"
    )
}

tasks.test { useJUnitPlatform() }

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
            )
        )
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(11)) }
    withJavadocJar()
    withSourcesJar()
}

val publishDirPath = layout.buildDirectory.dir("xesar-connect-artifact")

publishing {
    publications {
        create<MavenPublication>("library") {
            pom {
                name.set(project.name)
                url.set("https://github.com/open200/xesar-connect/")
                description.set(
                    "The Xesar-Connect library is an open-source Kotlin wrapper designed to simplify MQTT communication for seamless integration of the EVVA Xesar access control system with other software applications."
                )
                scm {
                    url.set("https://github.com/open200/xesar-connect")
                    connection.set("scm:git:git://github.com/open200/xesar-connect")
                    developerConnection.set("scm:git:ssh:git@github.com/open200/xesar-connect")
                }
                developers {
                    developer {
                        name.set("open200")
                        email.set("maven@open200.com")
                    }
                }
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.html")
                    }
                }
            }
            groupId = "com.open200"
            artifactId = "xesar-connect"
            from(components["java"])
        }
    }
    repositories { maven { url = uri(publishDirPath) } }
}

tasks.register<Zip>("zipArtifact") {
    description = "Creates a zip archive containing the xesar-connect artifact."
    group = "publishing"
    destinationDirectory.set(layout.buildDirectory)
    from(publishDirPath)
}

tasks.register("prepareMavenPublish") {
    dependsOn(setOf("publish", "zipArtifact"))
    group = "publishing"
}

signing { sign(publishing.publications["library"]) }

spotless {
    // optional: limit format enforcement to just the files changed by this feature branch
    //    ratchetFrom("origin/main")

    format("misc") {
        // define the files to apply `misc` to
        target("*.gradle", "*.md", ".gitignore")

        // define the steps to apply to those files
        trimTrailingWhitespace()
        leadingSpacesToTabs() // or spaces. Takes an integer argument if you don't like 4
        endWithNewline()
    }
    kotlin {
        // by default the target is every '.kt' and '.kts` file in the java sourcesets
        ktfmt().kotlinlangStyle() //we use kotlinlang Style for 4 spaces in tabs
        targetExclude("**/build/**/*")
    }
}
