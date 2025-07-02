package com.bb.builds.di

import com.bb.builds.data.KtorApi
import com.bb.builds.data.KtorApiImpl
import com.bb.builds.data.RemoteApi
import com.bb.builds.service.AutomateBuildSystem
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin() = startKoin {
    modules(
        apiModule,
        serviceModule,
    )
}

val apiModule = module {
    single<KtorApi> { KtorApiImpl() }
    factory { RemoteApi(get()) }
}

val serviceModule = module {
    factory { AutomateBuildSystem(get()) }
}
