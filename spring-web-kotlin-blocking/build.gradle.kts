plugins {
    `java-test-fixtures`
    kotlin("plugin.spring")
    kotlin("plugin.jpa")

    id("org.asciidoctor.convert")
}

sourceSets {
    // 이 설정이 없으면 test sourceSet에서 main을 인식하지 못한다. 원래 자동으로 해야하는데.. 왜지?
    test.get().compileClasspath += sourceSets.main.get().output
    test.get().runtimeClasspath += sourceSets.main.get().output

    testFixtures.get().compileClasspath += sourceSets.main.get().output
    testFixtures.get().runtimeClasspath += sourceSets.main.get().output
}

///////// spring restdocs ///////////
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
/////////////////////////////////////

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("com.h2database:h2:1.4.199")
    runtimeOnly("mysql:mysql-connector-java")

    asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

}
