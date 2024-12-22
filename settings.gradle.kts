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
include (":core:model")
include (":core:database")
//include (":feature:auth")
include (":feature:home")
//include (":feature:note")
