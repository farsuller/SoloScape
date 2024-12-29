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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io")  }
    }
}
rootProject.name = "SoloScape"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include (":app")
include (":core:ui")
include (":core:util")
include (":core:database")
include (":core:messagebar")
include (":feature:felt")
include (":feature:dashboard")
include (":feature:idea")

