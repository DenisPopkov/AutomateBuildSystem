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
import kotlinx.serialization.builtins.serializer

class RemoteApi(
    private val ktorApi: KtorApi,
) : KtorApi by ktorApi {

    suspend fun buildMac(
        buildData: BuildData,
    ): HttpResponse = client.post {
        apiUrl("build_mac")
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

    suspend fun buildIOS(
        buildData: BuildData,
    ): HttpResponse = client.post {
        apiUrl("build_ios")
        setBody(buildData)
        json()
    }

    suspend fun sendBuild(
        buildId: BuildId,
    ): HttpResponse = client.post {
        apiUrl("send_build")
        setBody(buildId)
        json()
    }

    suspend fun getBuilds(): List<BuildItem> =
        client.get {
            apiUrl("builds")
            contentType(ContentType.Application.Json)
            json()
        }.body()

    suspend fun getBranches(): Branches =
        client.get {
            apiUrl("remote_branches")
            contentType(ContentType.Application.Json)
            json()
        }.body()

    suspend fun stopProcess(
        processName: String,
    ): Branches =
        client.post {
            apiUrl("stop_process")
            setBody(processName)
            json()
        }.body()

}
