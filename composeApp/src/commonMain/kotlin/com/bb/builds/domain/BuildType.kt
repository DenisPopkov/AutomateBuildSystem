package com.bb.builds.domain

import com.bb.builds.domain.BuildType.ANDROID
import com.bb.builds.domain.BuildType.IOS
import com.bb.builds.domain.BuildType.MACOS
import com.bb.builds.domain.BuildType.WINDOWS

enum class BuildType(val buildName: String) {
    ANDROID(buildName = "Android"),
    IOS(buildName = "iOS"),
    MACOS(buildName = "MacOS"),
    WINDOWS(buildName = "Windows"),
    ;
}

fun getBuildScript(buildType: BuildType, sign: Boolean = false): String {
    return when (buildType) {
        ANDROID -> "./build_android.sh"
        IOS -> "./build_ios.sh"
        MACOS -> if (sign) "./build_mac_signed.sh" else "./build_mac_no_sign.sh"
        WINDOWS -> "./build_windows.sh"
    }
}

