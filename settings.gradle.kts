pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Kaho Ji"
include(":app")
include(":core-navigation")
include(":feature-splash")
include(":core-ui")
include(":core-common")
include(":feature-onboarding")
include(":feature-bottom-nav")
include(":feature-home")
include(":feature-auth-host")
include(":feature-home-host")
include(":core-network")
include(":feature-auth-user")
include(":feature-story")
include(":feature-chat")
include(":core-socket")
include(":core-auth")
include(":core-agora")
include(":feature-call")
include(":feature-wallet")
include(":feature-referral")
include(":feature-gift")
include(":core-earning")
include(":feature-withdraw")
include(":feature-block")
include(":feature-change-password")
include(":feature-notification")
include(":feature-review")
include(":feature-leaderboard")
include(":core-story")
include(":feature-cms")
include(":feature-help-support")
include(":core-fcm")
