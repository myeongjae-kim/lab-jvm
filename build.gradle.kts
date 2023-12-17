import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    jacoco
    `java-test-fixtures`

    id(Libs.Plugins.springBoot) version Libs.Versions.springBoot
    id(Libs.Plugins.springDependencyManagement) version Libs.Versions.springDependencyManagement
    id(Libs.Plugins.asciidoctorConvert) version Libs.Versions.asciidoctorConvert
    id(Libs.Plugins.ktlint) version Libs.Versions.ktlint
    id(Libs.Plugins.ktlintIdea) version Libs.Versions.ktlint
    id(Libs.Plugins.kotlinJvm) version Libs.Versions.kotlin
    id(Libs.Plugins.kotlinSpring) version Libs.Versions.kotlin
    id(Libs.Plugins.kotlinJpa) version Libs.Versions.kotlin
    id(Libs.Plugins.kotlinAllopen) version Libs.Versions.kotlin
    id(Libs.Plugins.kotlinNoArg) version Libs.Versions.kotlin
}

group = "kim.myeongjae"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

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
    apply {
        plugin("java-test-fixtures")
        plugin("org.gradle.jacoco")

        plugin(Libs.Plugins.kotlinJvm)
        plugin(Libs.Plugins.ktlint)
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "21"
        }
    }

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
            tasks.ktlintTestFixturesSourceSetCheck,
        )
        setDependsOn(dependsOn.filter { !excludedTasks.contains(it) })
    }

    tasks.test {
        finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    }
    tasks.jacocoTestReport {
        dependsOn(tasks.test) // tests are required to run before generating the report
    }

    val jacocoExcludePatterns = listOf("**/*ApplicationKt.class", "**/Constants.class")

    tasks.withType<JacocoCoverageVerification> {
        afterEvaluate {
            classDirectories.setFrom(
                files(
                    classDirectories.files.map {
                        fileTree(it).apply {
                            exclude(jacocoExcludePatterns)
                        }
                    },
                ),
            )
        }
    }

    tasks.withType<JacocoReport> {
        afterEvaluate {
            classDirectories.setFrom(
                files(
                    classDirectories.files.map {
                        fileTree(it).apply {
                            exclude(jacocoExcludePatterns)
                        }
                    },
                ),
            )
        }
    }

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        testImplementation(platform(Libs.Test.junitBom))
        testImplementation(Libs.Test.junit)
        testImplementation(Libs.Test.kotest)
        testImplementation(Libs.Test.kotestAssertionsCore)
        testImplementation(Libs.Test.kotestProperty)
    }
}

val springProjects = listOf(project(Libs.Module.App.springWebKotlinBlocking))
configure(springProjects) {
    apply {
        plugin(Libs.Plugins.springBoot)
        plugin(Libs.Plugins.springDependencyManagement)
        plugin(Libs.Plugins.kotlinSpring)
        plugin(Libs.Plugins.asciidoctorConvert)
    }

    val intTestImplementation = setupIntTestSourceSet(this)

    configurations["intTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

    tasks.check {
        dependsOn(tasks.ktlintFormat, intTestImplementation)
    }

    dependencies {
        implementation(Libs.Kotlin.reflect)
        implementation(Libs.Kotlin.kotlinJackson)

        developmentOnly(Libs.SpringBoot.devTools)
        Libs.SpringBoot.starterTest.run {
            implementation(this)
            intTestImplementation(this)
            testFixturesImplementation(this)
        }
    }
}

val springApiProjects = listOf(project(Libs.Module.App.springWebKotlinBlocking))
configure(springApiProjects) {
    apply {
        plugin(Libs.Plugins.asciidoctorConvert)
    }

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

    val asciidoctorExt: Configuration by configurations.creating
    dependencies {
        asciidoctorExt(Libs.Test.restdocsAsciidoctor)
        testImplementation(Libs.Test.restdocsMockMvc)
    }
}

fun setupIntTestSourceSet(vararg projects: Project): Configuration {
    lateinit var intTestImplementationToReturn: Configuration

    configure(projects.toList()) {
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
            isCanBeResolved = true
        }
        intTestImplementationToReturn = intTestImplementation

        configurations["intTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

        dependencies {
            intTestImplementation(Libs.SpringBoot.starterTest) {
                exclude(module = "mockito-core")
            }
            intTestImplementation(Libs.Test.kotest)
            intTestImplementation(Libs.Test.kotestAssertionsCore)
            intTestImplementation(Libs.Test.kotestProperty)
        }

        val integrationTest = task<Test>("intTest") {
            description = "Runs integration tests."
            group = "verification"

            testClassesDirs = sourceSets["intTest"].output.classesDirs
            classpath = sourceSets["intTest"].runtimeClasspath
            shouldRunAfter("test")
        }

        tasks.check { dependsOn(integrationTest) }
    }

    return intTestImplementationToReturn
}
