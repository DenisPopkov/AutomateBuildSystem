package com.bb.builds.domain

enum class BuildType(val buildName: String) {
    ANDROID(buildName = "Android"),
    IOS(buildName = "iOS"),
    MACOS(buildName = "macOS"),
    WINDOWS(buildName = "Windows"),
    ;

    companion object {
        fun getBuildType(platformName: String): BuildType {
            return entries.firstOrNull { it.buildName.equals(platformName, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown platform name: $platformName")
        }
    }
}
