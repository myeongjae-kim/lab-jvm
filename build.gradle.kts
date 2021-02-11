import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
    kotlin("plugin.jpa") version "1.4.21"
    `java-test-fixtures`
}

group = "kim.myeongjae"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    repositories {
        mavenCentral()
    }
}

val springProjects = listOf(project(":spring-web-kotlin-blocking"))
configure(springProjects) {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.gradle.java-test-fixtures")

    sourceSets {
        create("intTest") {
            compileClasspath += sourceSets.main.get().output
            runtimeClasspath += sourceSets.main.get().output

            compileClasspath += sourceSets.testFixtures.get().output
            runtimeClasspath += sourceSets.testFixtures.get().output
        }
    }

    val intTestImplementation by configurations.getting {
        extendsFrom(configurations.implementation.get())
    }

    configurations["intTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())


    tasks.withType<Test> {
        useJUnitPlatform()
    }

    val integrationTest = task<Test>("integrationTest") {
        description = "Runs integration tests."
        group = "verification"

        testClassesDirs = sourceSets["intTest"].output.classesDirs
        classpath = sourceSets["intTest"].runtimeClasspath
        shouldRunAfter("test")
    }

    tasks.check { dependsOn(integrationTest) }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        developmentOnly("org.springframework.boot:spring-boot-devtools")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        intTestImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

