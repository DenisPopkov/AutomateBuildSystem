package com.bb.builds.service

import com.bb.builds.data.RemoteApi
import com.bb.builds.domain.Branches
import com.bb.builds.domain.BuildData
import com.bb.builds.domain.BuildId
import com.bb.builds.domain.BuildItem
import io.ktor.client.statement.HttpResponse

class AutomateBuildSystem(
    private val remoteApi: RemoteApi
) {
    suspend fun buildMac(buildData: BuildData): HttpResponse? =
        remoteApi.buildMac(buildData)

    suspend fun buildAndroid(buildData: BuildData): HttpResponse? =
        remoteApi.buildAndroid(buildData)

    suspend fun buildIOS(buildData: BuildData): HttpResponse? =
        remoteApi.buildIOS(buildData)

    suspend fun getBuilds(): List<BuildItem>? =
        remoteApi.getBuilds()

    suspend fun sendBuild(buildId: BuildId): HttpResponse? =
        remoteApi.sendBuild(buildId)

    suspend fun getBranches(): Branches? =
        remoteApi.getBranches()
}
