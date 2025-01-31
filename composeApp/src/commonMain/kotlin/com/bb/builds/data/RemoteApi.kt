package com.bb.builds.data

import com.bb.builds.domain.Branches
import com.bb.builds.domain.BuildData
import com.bb.builds.domain.BuildId
import com.bb.builds.domain.BuildItem
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.delay

class RemoteApi(
    private val ktorApi: KtorApi
) : KtorApi by ktorApi {

    private suspend fun <T> retryApiCall(
        retries: Int = 3,
        block: suspend () -> T
    ): T? {
        repeat(retries - 1) {
            try {
                return block()
            } catch (e: Exception) {
                delay(2000)
            }
        }
        return try {
            block()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun buildMac(buildData: BuildData): HttpResponse? =
        runCatching {
            client.post {
                apiUrl("build_mac")
                setBody(buildData)
                json()
            }
        }.getOrNull()

    suspend fun buildAndroid(buildData: BuildData): HttpResponse? =
        runCatching {
            client.post {
                apiUrl("build_android")
                setBody(buildData)
                json()
            }
        }.getOrNull()

    suspend fun buildIOS(buildData: BuildData): HttpResponse? =
        runCatching {
            client.post {
                apiUrl("build_ios")
                setBody(buildData)
                json()
            }
        }.getOrNull()

    suspend fun sendBuild(buildId: BuildId): HttpResponse? =
        runCatching {
            client.post {
                apiUrl("send_build")
                setBody(buildId)
                json()
            }
        }.getOrNull()

    suspend fun getBuilds(): List<BuildItem>? =
        retryApiCall {
            client.get {
                apiUrl("builds")
                contentType(ContentType.Application.Json)
                json()
            }.body()
        }

    suspend fun getBranches(): Branches? =
        retryApiCall {
            client.get {
                apiUrl("remote_branches")
                contentType(ContentType.Application.Json)
                json()
            }.body()
        }
}
