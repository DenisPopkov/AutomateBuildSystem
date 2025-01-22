package com.bb.builds.data

import com.bb.builds.domain.BuildData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class RemoteApi(
    private val ktorApi: KtorApi,
) : KtorApi by ktorApi {

    suspend fun buildMac(
        buildData: BuildData,
    ): HttpResponse = client.post {
        apiUrl("build_macos")
        setBody(buildData)
        json()
    }

    suspend fun buildAndroid(
        buildData: BuildData,
    ): HttpResponse = client.post {
        apiUrl("build_android")
        setBody(buildData)
        json()
    }

}
