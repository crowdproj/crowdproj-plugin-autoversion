group = "com.crowdproj.generator"
version = "0.0.1"

repositories {
    mavenCentral()
}

tasks {
    create("deploy") {
        group = "build"
        dependsOn(gradle.includedBuild("crowdproj-autoversion-plugin").task(":deploy"))
    }
}
