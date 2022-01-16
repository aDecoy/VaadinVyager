import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.gretty") version "3.0.6"
    war
    id("com.vaadin") version "23.0.0.alpha2"
}

val karibudsl_version = "1.1.1"
val vaadin_version = "23.0.0.alpha2"

defaultTasks("clean", "build")

repositories {
    mavenCentral()
    maven { setUrl("https://maven.vaadin.com/vaadin-addons") }
    maven { setUrl("https://maven.vaadin.com/vaadin-prereleases") }
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
    // managedClassReload = true // temporarily disabled because of https://github.com/gretty-gradle-plugin/gretty/issues/166
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // to see the exceptions of failed tests in Travis-CI console.
        exceptionFormat = TestExceptionFormat.FULL
    }
}

val staging by configurations.creating

dependencies {
    // Karibu-DSL dependency
    implementation("com.github.mvysny.karibudsl:karibu-dsl:$karibudsl_version")
    implementation("com.github.mvysny.karibu-tools:karibu-tools:0.8")

    // Vaadin
    implementation("com.vaadin:vaadin-core:${vaadin_version}") {
        exclude(module = "fusion-endpoint") // exclude fusion: it brings tons of dependencies (including swagger)
    }
    providedCompile("javax.servlet:javax.servlet-api:4.0.1")

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:1.7.32")

    implementation(kotlin("stdlib-jdk8"))

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v10:1.3.9")
    testImplementation("com.github.mvysny.dynatest:dynatest:0.24")

    // heroku app runner
    staging("com.heroku:webapp-runner-main:9.0.52.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// Heroku
tasks {
    val copyToLib by registering(Copy::class) {
        into("$buildDir/server")
        from(staging) {
            include("webapp-runner*")
        }
    }
    val stage by registering {
        dependsOn("build", copyToLib)
    }
}

vaadin {
    if (gradle.startParameter.taskNames.contains("stage")) {
        productionMode = true
    }
}
