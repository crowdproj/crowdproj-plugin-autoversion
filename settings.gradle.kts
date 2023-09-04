pluginManagement {
    plugins {
        val kotlinVersion: String by settings

        kotlin("jvm") version kotlinVersion
    }
}

rootProject.name = "crowdproj-plugin-autoversion-root"

includeBuild("crowdproj-autoversion-plugin")
include("crowdproj-autoversion-test")
