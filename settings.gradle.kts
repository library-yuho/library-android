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
        maven ("https://www.jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repository.map.naver.com/archive/maven")
    }
}

rootProject.name = "ibooku"
include(":app")
include(":data")
include(":domain")
include(":presentation")
include(":base")
include(":core")
