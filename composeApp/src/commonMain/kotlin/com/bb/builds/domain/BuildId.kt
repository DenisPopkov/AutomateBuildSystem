package com.bb.builds.domain

import kotlinx.serialization.Serializable

@Serializable
data class BuildId(
    val buildId: Int,
)