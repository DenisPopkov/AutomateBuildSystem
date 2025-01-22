package com.bb.builds.service

import com.bb.builds.data.RemoteApi
import com.bb.builds.domain.BuildData
import io.ktor.client.statement.HttpResponse

class AutomateBuildSystem(
    private val remoteApi: RemoteApi,
) {
    suspend fun buildMac(
        buildData: BuildData,
    ): HttpResponse = remoteApi.buildMac(
        buildData = buildData,
    )

    suspend fun buildAndroid(
        buildData: BuildData,
    ): HttpResponse = remoteApi.buildAndroid(
        buildData = buildData,
    )

}
