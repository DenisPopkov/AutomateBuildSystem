package com.bb.builds.data

import com.bb.builds.domain.Branches
import com.bb.builds.domain.BuildData
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isRelativePath
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

    private var windowsUrl: String? = null
    private var macX86Url: String? = null
    private var macM1Url: String? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchServerUrlFromGitHub()
        }
    }

    suspend fun buildMac(buildData: BuildData, isX86: Boolean): HttpResponse? =
        runCatching {
            val url = if (isX86) macX86Url else macM1Url
            url ?: return@runCatching null
            client.post {
                url {
                    takeFrom(url)
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
                    takeFrom(macM1Url ?: macX86Url ?: windowsUrl ?: "")
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
                    takeFrom(macM1Url ?: macX86Url ?: windowsUrl ?: "")
                    encodedPath = "build_ios"
                }
                setBody(buildData)
                json()
            }
        }.getOrNull()

    suspend fun buildWin(buildData: BuildData): HttpResponse? =
        runCatching {
            val url = windowsUrl ?: return@runCatching null
            client.post {
                url {
                    takeFrom(url)
                    encodedPath = "build_win"
                }
                setBody(buildData)
                json()
            }
        }.getOrNull()

    suspend fun rebuildDSPLibrary(
        buildData: BuildData,
        isWindows: Boolean,
        isX86: Boolean = false
    ): HttpResponse? =
        runCatching {
            val url = when {
                isWindows -> windowsUrl
                isX86 -> macX86Url
                else -> macM1Url
            }
            url ?: return@runCatching null
            client.post {
                url {
                    takeFrom(url)
                    encodedPath = "rebuild_dsp"
                }
                setBody(buildData)
                json()
            }
        }.getOrNull()

    suspend fun rebuildAndroidDSPLibrary(buildData: BuildData): HttpResponse? =
        runCatching {
            client.post {
                url {
                    takeFrom(macX86Url ?: windowsUrl ?: "")
                    encodedPath = "rebuild_android_dsp"
                }
                setBody(buildData)
                json()
            }
        }.getOrNull()

    suspend fun getBranches(): Branches? =
        retryApiCall {
            client.get {
                url {
                    takeFrom(macM1Url ?: macX86Url ?: windowsUrl ?: "")
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
                windowsUrl = config["windows_url"]?.jsonPrimitive?.content
                macX86Url = config["mac_x86_url"]?.jsonPrimitive?.content
                macM1Url = config["mac_m1_url"]?.jsonPrimitive?.content
            }
        } catch (_: Exception) {
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
}
