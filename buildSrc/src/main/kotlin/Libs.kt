val isMac: Boolean
    get() = org.gradle.internal.os.OperatingSystem.current().isMacOsX

object Libs {
    object Plugins {
        const val springBoot = "org.springframework.boot"
        const val springDependencyManagement = "io.spring.dependency-management"
        const val ktlint = "org.jlleitschuh.gradle.ktlint"
        const val ktlintIdea = "org.jlleitschuh.gradle.ktlint-idea"
        const val kotlinJvm = "org.jetbrains.kotlin.jvm"
        const val kotlinSpring = "org.jetbrains.kotlin.plugin.spring"
        const val kotlinJpa = "org.jetbrains.kotlin.plugin.jpa"
        const val kotlinAllopen = "org.jetbrains.kotlin.plugin.allopen"
        const val kotlinNoArg = "org.jetbrains.kotlin.plugin.noarg"
        const val asciidoctorConvert = "org.asciidoctor.jvm.convert"
    }

    object Versions {
        const val springBoot = "2.7.12"
        const val springDependencyManagement = "1.0.15.RELEASE"
        const val kotlin = "1.9.20"
        const val ktlint = "11.6.1"
        const val kotest = "5.8.0"
        const val asciidoctorConvert = "3.3.2"
        const val restdocs = "2.0.6.RELEASE"
        const val kotlinCoroutineVersion = "1.8.0-RC"
    }

    object SpringBoot {
        const val starterTest = "org.springframework.boot:spring-boot-starter-test"
        const val devTools = "org.springframework.boot:spring-boot-devtools"
    }

    object Kotlin {
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutineVersion}"
    }

    object Test {
        const val junitBom = "org.junit:junit-bom:5.9.1"
        const val junit = "org.junit.jupiter:junit-jupiter"

        const val kotest = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
        const val kotestAssertionsCore = "io.kotest:kotest-assertions-core:${Versions.kotest}"
        const val kotestProperty = "io.kotest:kotest-property:${Versions.kotest}"
        const val restdocsAsciidoctor = "org.springframework.restdocs:spring-restdocs-asciidoctor:${Versions.restdocs}"
        const val restdocsMockMvc = "org.springframework.restdocs:spring-restdocs-mockmvc"
    }

    object Module {
        object App {
            const val springWebKotlinBlocking = ":spring-web-kotlin-blocking"
        }
    }

    const val h2 = "com.h2database:h2"
    const val jpa = "jakarta.persistence:jakarta.persistence-api"
}
