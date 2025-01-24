package com.bb.builds.domain

import kotlinx.serialization.Serializable

@Serializable
data class Branches(
    val branches: List<String>,
)
