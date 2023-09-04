plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `kotlin-dsl`
    id("com.gradle.plugin-publish")
    id("org.jetbrains.dokka")
}

group = "com.crowdproj.plugin.autoversion"
version = "0.0.2"

repositories {
    mavenCentral()
}

dependencies {
    val grgitVersion: String by project

    implementation("org.ajoberstar.grgit:grgit-gradle:$grgitVersion")

    testImplementation(kotlin("test"))
}

gradlePlugin {
    @Suppress("UnstableApiUsage")
    website.set("https://github.com/crowdproj/crowdproj-plugin-autoversion")
    @Suppress("UnstableApiUsage")
    vcsUrl.set("https://github.com/crowdproj/crowdproj-plugin-autoversion.git")
    plugins {
        create("com.crowdproj.plugin.autoversion") {
            id = "com.crowdproj.plugin.autoversion"
            displayName = "CrowdProj Autoversion Gradle plugin"
            description = "This plugin automatically increments version of the project for branches like release/1.2"
            @Suppress("UnstableApiUsage")
            tags.set(listOf("gradle", "crowdproj", "kotlin", "version", "git", "gitflow"))
            implementationClass = "com.crowdproj.plugin.autoversion.CrowdprojAutoversionPlugin"
            version = project.version
        }
    }
}

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    group = "publishing"
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        reports {
            junitXml.required.set(true)
        }
    }

    publishPlugins {
        dependsOn(build)
    }

    create("deploy") {
        group = "build"
        dependsOn(publishPlugins)
    }

}

kotlin {
    jvmToolchain(17)
}
