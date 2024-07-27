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

        maven("https://jitpack.io")  //这句话是关键代码,辅助工具
        maven("https://maven.aliyun.com/repository/public")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven("https://jitpack.io")  //这句话是关键代码,辅助工具
        maven("https://maven.aliyun.com/repository/public")
    }
}

rootProject.name = "ianlee-baselib"
include(":app")
include(":baselib")
