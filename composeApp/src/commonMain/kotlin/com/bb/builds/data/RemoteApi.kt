package com.bb.builds.data

import com.bb.builds.domain.BuildData
import io.ktor.client.request.*

class RemoteApi(
    private val ktorApi: KtorApi,
) : KtorApi by ktorApi {

    suspend fun buildMac(
        buildData: BuildData,
    ) = client.post {
        apiUrl("build_mac")
        setBody(buildData)
        json()
    }

}
