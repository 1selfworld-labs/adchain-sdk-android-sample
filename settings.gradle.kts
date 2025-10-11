pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Adjoe SDK Maven repository
        maven { url = uri("https://releases.adjoe.io/maven") }
    }
}

rootProject.name = "AdchainSample"
include(":app")
include(":adchain-sdk")
project(":adchain-sdk").projectDir = file("../adchain-sdk-android/adchain-sdk")