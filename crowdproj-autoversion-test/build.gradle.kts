plugins {
    kotlin("jvm")
    id("com.crowdproj.plugin.autoversion")
//    java
}

group = rootProject.group
version = "0.0.0-dev"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-common"))
    testImplementation(kotlin("test-junit"))
}

autoversion {
//    releaseRe.set(Regex("^mai(\\w+)$"))
}

tasks {
    build {
        mustRunAfter(addGitVersionTag)
        mustRunAfter(pushGitVersionTag)
    }
}

afterEvaluate {
    println("CURRENT VERSION is ${project.version}")
}
