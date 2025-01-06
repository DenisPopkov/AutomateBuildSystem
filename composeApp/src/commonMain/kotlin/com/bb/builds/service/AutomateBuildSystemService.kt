package com.bb.builds.service

import com.bb.builds.utils.get
import io.ktor.client.HttpClient
import io.ktor.http.path

class AutomateBuildSystem(private val client: HttpClient) {
    suspend fun buildAndroid(): Result<Unit> = client
        .get { url { path("") } }
}
