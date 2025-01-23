package com.bb.builds.domain

enum class BuildType(val buildName: String) {
    ANDROID(buildName = "Android"),
    IOS(buildName = "iOS"),
    MACOS(buildName = "macOS"),
    WINDOWS(buildName = "Windows"),
    ;
}
