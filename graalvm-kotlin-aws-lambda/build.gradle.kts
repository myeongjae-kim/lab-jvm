plugins {
    id(Libs.Plugins.shadowJar) version (Libs.Versions.shadowJarVersion)
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("kim.myeongjae.graalvmkotlin.awslambda.MainKt")
}

dependencies {
}
