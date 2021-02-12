import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-test-fixtures`
    jacoco

    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("org.asciidoctor.convert") version "2.4.0"

    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    kotlin("plugin.jpa") version "1.4.30"
    kotlin("plugin.allopen") version "1.4.30"
}

group = "kim.myeongjae"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

tasks.ktlintFormat {
    group = "verification"
}

tasks.ktlintCheck {
    group = "other"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.gradle.jacoco")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    // ////////// ktlint //////////
    tasks.ktlintFormat {
        group = "verification"
    }

    tasks.ktlintCheck {
        group = "other"
    }

    tasks.check {
        val excludedTasks = listOf(
            tasks.ktlintMainSourceSetCheck,
            tasks.ktlintTestSourceSetCheck,
            tasks.ktlintTestFixturesSourceSetCheck
        )
        setDependsOn(dependsOn.filter { !excludedTasks.contains(it) })
    }
    // ////////////////////////////

    // ////////// jacoco //////////
    tasks.test {
        finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    }
    tasks.jacocoTestReport {
        dependsOn(tasks.test) // tests are required to run before generating the report
    }

    val jacocoExcludePatterns = listOf("**/*ApplicationKt.class", "**/Constants.class")

    tasks.withType<JacocoCoverageVerification> {
        afterEvaluate {
            classDirectories.setFrom(files(classDirectories.files.map {
                fileTree(it).apply {
                    exclude(jacocoExcludePatterns)
                }
            }))
        }
    }

    tasks.withType<JacocoReport> {
        afterEvaluate {
            classDirectories.setFrom(files(classDirectories.files.map {
                fileTree(it).apply {
                    exclude(jacocoExcludePatterns)
                }
            }))
        }
    }
    // ////////////////////////////

    repositories {
        mavenCentral()
    }
}

// ///////// spring projects common configuration ///////////
val springProjects = listOf(project(":spring-web-kotlin-blocking"))
configure(springProjects) {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.gradle.java-test-fixtures")

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // ///////// intTest sourceSet ///////////
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

    val integrationTest = task<Test>("integrationTest") {
        description = "Runs integration tests."
        group = "verification"

        testClassesDirs = sourceSets["intTest"].output.classesDirs
        classpath = sourceSets["intTest"].runtimeClasspath
        shouldRunAfter("test")
    }

    tasks.check {
        dependsOn(tasks.ktlintFormat, integrationTest)
    }
    // ///////////////////////////////////////

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        developmentOnly("org.springframework.boot:spring-boot-devtools")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
        intTestImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}
// //////////////////////////////////////////////////////////

// ///////// spring api projects common configuration ///////////
val springApiProjects = listOf(project(":spring-web-kotlin-blocking"))
configure(springApiProjects) {
    apply(plugin = "org.asciidoctor.convert")

    sourceSets {
        // 이 설정이 없으면 test sourceSet에서 main을 인식하지 못한다. 원래 자동으로 해야하는데.. 왜지?
        test.get().compileClasspath += sourceSets.main.get().output
        test.get().runtimeClasspath += sourceSets.main.get().output

        testFixtures.get().compileClasspath += sourceSets.main.get().output
        testFixtures.get().runtimeClasspath += sourceSets.main.get().output
    }

    // ///////// spring restdocs ///////////
    val snippetsDir = file("build/generated-snippets")

    tasks.test {
        outputs.dir(snippetsDir)
    }

    tasks.asciidoctor {
        inputs.dir(snippetsDir)
        dependsOn(tasks.test)
    }

    tasks.bootJar {
        dependsOn(tasks.asciidoctor)
        from("${tasks.asciidoctor.get().outputDir}/html5") {
            into("static/docs")
        }
    }
    // /////////////////////////////////////

    dependencies {
        asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor")
        testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    }
}
// //////////////////////////////////////////////////////////////
