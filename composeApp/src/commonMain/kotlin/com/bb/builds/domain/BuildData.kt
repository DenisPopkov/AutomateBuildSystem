package com.bb.builds.domain

import kotlinx.serialization.Serializable

@Serializable
data class BuildData(
    val branchName: String,
    val isUseDevAnalytics: Boolean,
)
