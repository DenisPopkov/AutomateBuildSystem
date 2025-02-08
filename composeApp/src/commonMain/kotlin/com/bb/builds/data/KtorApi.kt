package com.bb.builds.data

import io.ktor.client.*
import io.ktor.client.request.*

interface KtorApi {
    val client: HttpClient
    fun HttpRequestBuilder.json()
}