package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParserBuilder
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.CommonErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient.HttpClientConfigPlugin
import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import kotlinx.coroutines.CoroutineScope

class CommonComponent(
    val encryptedSettings: EncryptedSettings,
    deviceInfo: DeviceInfo,
    baseUrl: String,
    httpClientConfigPlugins: List<HttpClientConfigPlugin> = emptyList(),
    private val accessTokenProvider: AccessTokenProvider,
    appScope: CoroutineScope,
    platformContext: Any? = null
) {

    @InternalApi
    constructor(
        encryptedSettings: EncryptedSettings,
        deviceInfo: DeviceInfo,
        baseUrl: String,
        accessTokenProvider: AccessTokenProvider,
        appScope: CoroutineScope
    ) : this(
        encryptedSettings,
        deviceInfo,
        baseUrl,
        emptyList(),
        accessTokenProvider,
        appScope,
        null
    )

    private var _appErrorParser: AppErrorParser? = null
    val appErrorParser: AppErrorParser
        get() = _appErrorParser ?: throw IllegalStateException(
            "AppErrorParser not initialized! Call init() first."
        )

    private val storageModule by lazy { CommonStorageModule(encryptedSettings) }
    val commonStorage get() = storageModule.commonStorage

    private val platformModule by lazy { CommonPlatformModule(platformContext) }
    val externalLauncher get() = platformModule.externalLauncher

    private val repositoryModule by lazy {
        CommonRepositoryModule(
            deviceInfo
        )
    }
    val platformRepository get() = repositoryModule.platformRepository

    private val networkModule by lazy {
        CommonNetworkModule(
            baseUrl = baseUrl,
            httpClientConfigPlugins = httpClientConfigPlugins,
            accessTokenProvider = accessTokenProvider,
            platformRepository = platformRepository,
            appScope = appScope
        )
    }
    val httpClient get() = networkModule.httpClient
    val webSocketService get() = networkModule.webSocketService
    val commonWebSocketMessageHandler get() = networkModule.commonWebSocketMessageHandler

    fun init(
        appErrorParserCommonParser: AppErrorParser = CommonErrorParser,
        appErrorParserSpecificParsers: List<AppErrorParser> = emptyList()
    ) {
        _appErrorParser = AppErrorParserBuilder.build(
            commonParser = appErrorParserCommonParser,
            specificParsers = appErrorParserSpecificParsers
        )
    }
}