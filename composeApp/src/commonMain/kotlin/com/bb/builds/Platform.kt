package com.bb.builds

interface Platform {
    val name: String
}

val isDesktop = getPlatform().name == "macOS" || getPlatform().name == "Windows"

expect fun getPlatform(): Platform