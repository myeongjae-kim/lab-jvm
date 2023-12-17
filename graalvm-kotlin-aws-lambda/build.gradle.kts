plugins {
    id(Libs.Plugins.shadowJar) version (Libs.Versions.shadowJarVersion)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Libs.AWS.lambdaCore)
    implementation(Libs.AWS.lambdaEvents)
    implementation(Libs.lambdaRuntimeGraalvm)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.formkiq.lambda.runtime.graalvm.LambdaRuntime"
    }
}
