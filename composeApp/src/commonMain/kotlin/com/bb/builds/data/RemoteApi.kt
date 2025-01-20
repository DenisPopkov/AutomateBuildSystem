package com.bb.builds.data

import io.ktor.client.request.*

class RemoteApi(private val ktorApi: KtorApi) : KtorApi by ktorApi {

    suspend fun buildMac() = client.post {
        apiUrl("build_mac_signed")
        json()
    }
}