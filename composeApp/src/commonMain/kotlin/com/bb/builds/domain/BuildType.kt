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

fun getBuildScript(buildType: BuildType, sign: Boolean = false): Process {
    return Process(
        processName = when (buildType) {
            ANDROID -> "sh ./build_android.sh"
            IOS -> "sh ./build_ios.sh"
            MACOS -> if (sign) "sh ./build_mac_signed.sh" else "sh ./build_mac_no_sign.sh"
            WINDOWS -> "sh ./build_windows.sh"
        }
    )
}
