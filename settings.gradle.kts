pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven ("https://developer.huawei.com/repo/")
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven ("https://developer.huawei.com/repo/")
    }
}

rootProject.name = "HuaweiProject"
include(":app")
 