package com.bb.builds.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class KtorApiImpl : KtorApi {

    private val repoUrl =
        "https://api.github.com/repos/DenisPopkov/AutomateBuildSystem/contents/config.json"
    private var prodUrl =
        "https://55b6-143-198-25-149.ngrok-free.app"

    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchServerUrlFromGitHub()
        }
    }

    override val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    override fun HttpRequestBuilder.apiUrl(path: String) {
        url {
            takeFrom(prodUrl)
            encodedPath = path
        }
    }

    override fun HttpRequestBuilder.json() {
        contentType(ContentType.Application.Json)
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
        } catch (_: Exception) {
        }
    }
}
