package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParserBuilder
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.CommonErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient.HttpClientConfigPlugin
import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.ExternalLauncher
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.CommonStorage
import kotlinx.coroutines.CoroutineScope

/**
 * Root SDK wiring component for `core/common`.
 * This class is responsible for assembling and exposing the common infrastructure:
 * - encrypted settings access (`commonStorage`)
 * - platform abstractions (e.g. `externalLauncher`, `platformRepository`)
 * - networking bootstrap (`httpClient`, `webSocketService`)
 * - error parsing pipeline (`appErrorParser`) that is initialized via [init].
 * Constructor dependencies:
 * - [EncryptedSettings]: backing store for [CommonStorage].
 * - [DeviceInfo]: device identity for repositories and WebSocket bootstrap.
 * - `baseUrl`: HTTP and WebSocket endpoint base.
 * - [HttpClientConfigPlugin] list: optional extensions to the shared Ktor HTTP client (empty by default).
 * - [AccessTokenProvider]: token for authenticated HTTP and WebSocket.
 * - [CoroutineScope]: application scope for long-running SDK work (for example WebSockets).
 * - `platformContext`: optional platform handle for abstractions such as [ExternalLauncher]; may be null.
 * Host apps (see `sample/`) must call [init] before accessing [appErrorParser].
 */
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

    /**
     * Builds and installs the root [AppErrorParser] chain used by composition locals and UI.
     *
     * @param appErrorParserCommonParser Parser used when no feature parser handles an error (defaults to [CommonErrorParser]).
     * @param appErrorParserSpecificParsers Feature parsers consulted before the common parser.
     */
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