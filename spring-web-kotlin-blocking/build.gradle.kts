plugins {
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
}

repositories {
    maven { setUrl("https://jitpack.io") }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // https://github.com/FasterXML/jackson-module-kotlin/issues/199#issuecomment-1427990047
    implementation("com.github.ProjectMapK:jackson-module-kogera:2.16.0-beta8")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java:8.0.33")

    testFixturesImplementation("org.springframework.boot:spring-boot-starter-web")
}
