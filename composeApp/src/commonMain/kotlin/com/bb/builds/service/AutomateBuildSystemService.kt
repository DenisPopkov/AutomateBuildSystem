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
    suspend fun buildMac(buildData: BuildData, isX86: Boolean): HttpResponse? =
        remoteApi.buildMac(buildData, isX86)

    suspend fun buildAndroid(buildData: BuildData): HttpResponse? =
        remoteApi.buildAndroid(buildData)

    suspend fun buildIOS(buildData: BuildData): HttpResponse? =
        remoteApi.buildIOS(buildData)

    suspend fun buildWin(buildData: BuildData): HttpResponse? =
        remoteApi.buildWin(buildData)

    suspend fun getBranches(): Branches? =
        remoteApi.getBranches()

    suspend fun rebuildDSPLibrary(
        buildData: BuildData,
        isWindows: Boolean,
        isX86: Boolean,
    ): HttpResponse? =
        remoteApi.rebuildDSPLibrary(buildData, isWindows, isX86)

    suspend fun rebuildAndroidDSPLibrary(buildData: BuildData): HttpResponse? =
        remoteApi.rebuildAndroidDSPLibrary(buildData)

    suspend fun rebuildJARLibrary(buildData: BuildData): HttpResponse? =
        remoteApi.rebuildJARLibrary(buildData)

    suspend fun checkIOSCache(buildData: BuildData): HttpResponse? =
        remoteApi.checkIOSCache(buildData)
}
