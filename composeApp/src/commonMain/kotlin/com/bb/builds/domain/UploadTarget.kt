package com.bb.builds.domain

enum class UploadTarget(val targetName: String) {
    SLACK(targetName = "slack"),
    FIREBASE(targetName = "firebase"),
    R2(targetName = "r2"),
    ;
}
