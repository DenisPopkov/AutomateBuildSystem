package com.bb.builds.domain

import kotlinx.serialization.Serializable

@Serializable
data class BuildData(
    val branchName: String,
    val sign: Boolean = false, // in some targets not using
    val bumpVersion: Boolean = false, // in some targets not using
    val isBundleToBuild: Boolean = false, // in some targets not using
    val isUseDevAnalytics: Boolean,
)
