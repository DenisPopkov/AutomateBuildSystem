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
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class RemoteApi(
    private val ktorApi: KtorApi
) : KtorApi by ktorApi {

    private val repoUrl =
        "https://api.github.com/repos/DenisPopkov/AutomateBuildSystem/contents/config.json?ref=develop"
    private var prodUrl =
        "https://55b6-143-198-25-149.ngrok-free.app"

    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchServerUrlFromGitHub()
        }
    }

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
                url {
                    takeFrom(prodUrl)
                    encodedPath = "build_mac"
                }
                setBody(buildData)
                json()
            }
        }.getOrNull()

    suspend fun buildAndroid(buildData: BuildData): HttpResponse? =
        runCatching {
            client.post {
                url {
                    takeFrom(prodUrl)
                    encodedPath = "build_android"
                }
                setBody(buildData)
                json()
            }
        }.getOrNull()

    suspend fun buildIOS(buildData: BuildData): HttpResponse? =
        runCatching {
            client.post {
                url {
                    takeFrom(prodUrl)
                    encodedPath = "build_ios"
                }
                setBody(buildData)
                json()
            }
        }.getOrNull()

    suspend fun sendBuild(buildId: BuildId): HttpResponse? =
        runCatching {
            client.post {
                url {
                    takeFrom(prodUrl)
                    encodedPath = "send_build"
                }
                setBody(buildId)
                json()
            }
        }.getOrNull()

    suspend fun getBuilds(): List<BuildItem>? =
        retryApiCall {
            client.get {
                url {
                    takeFrom(prodUrl)
                    encodedPath = "builds"
                }
                contentType(ContentType.Application.Json)
                json()
            }.body()
        }

    suspend fun getBranches(): Branches? =
        retryApiCall {
            client.get {
                url {
                    takeFrom(prodUrl)
                    encodedPath = "remote_branches"
                }
                contentType(ContentType.Application.Json)
                json()
            }.body()
        }

    private suspend fun fetchServerUrlFromGitHub() {
        try {
            val response = client.get(repoUrl)
            val jsonResponse = response.bodyAsText()

            val fileContent = Json.parseToJsonElement(jsonResponse).jsonObject
            val fileUrl = fileContent["download_url"]?.jsonPrimitive?.content

            fileUrl?.let {
                val configResponse = client.get(it)
                val configJson = configResponse.bodyAsText()
                val config = Json.parseToJsonElement(configJson).jsonObject
                prodUrl = config["server_url"]?.jsonPrimitive?.content ?: prodUrl
            }
        } catch (_: Exception) {}
    }

}
