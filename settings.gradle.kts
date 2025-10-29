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
        // JitPack repository for AdChain SDK
        maven { url = uri("https://jitpack.io") }
        // Adjoe SDK Maven repository
        maven { url = uri("https://releases.adjoe.io/maven") }
    }
}

rootProject.name = "AdchainSample"
include(":app")

// local sdk 참조 할 경우 (현재는 원격 JitPack v1.0.29 사용)
// include(":adchain-sdk")
// project(":adchain-sdk").projectDir = file("../adchain-sdk-android/adchain-sdk")