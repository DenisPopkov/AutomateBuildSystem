package com.bb.builds.data

import com.bb.builds.domain.BuildData
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteApi(
    private val ktorApi: KtorApi,
) : KtorApi by ktorApi {

    suspend fun buildMac(
        buildData: BuildData,
    ) = client.post {
        runCatching {
            apiUrl("build_mac")
            setBody(buildData)
            json()
        }
    }

}
