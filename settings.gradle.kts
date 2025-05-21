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
    }
}

rootProject.name = "Combinia"
include(":app")
// İsterseniz bu modülleri de ekleyebilirsiniz
//include(":core")
//include(":data")
//include(":domain")