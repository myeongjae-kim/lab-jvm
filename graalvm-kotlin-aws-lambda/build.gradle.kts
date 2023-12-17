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

task("buildGraalVMImage") {
    inputs.files("${project.projectDir}/src/main", configurations.compileClasspath)
    outputs.upToDateWhen { file("${layout.buildDirectory}/graalvm/server").exists() }
    outputs.file(file("${layout.buildDirectory}/graalvm/server"))

    doLast {
        exec {
            commandLine("bash", "-c", "./build_graalvm.sh")
        }
    }

    dependsOn("shadowJar", "test")
}

tasks.build {
    dependsOn("buildGraalVMImage")
}
