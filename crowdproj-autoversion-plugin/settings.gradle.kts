pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val dokkaVersion: String by settings
        val pluginPublishVersion: String by settings

        kotlin("jvm") version kotlinVersion
        id("org.jetbrains.dokka") version dokkaVersion
        id("com.gradle.plugin-publish") version pluginPublishVersion
    }
}

rootProject.name = "crowdproj-plugin-autoversion"
