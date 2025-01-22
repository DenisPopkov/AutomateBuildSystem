package com.bb.builds.domain

import kotlinx.serialization.Serializable

@Serializable
data class BuildItem(
    val id: Int,
    val platformName: String,
    val version: String,
)
