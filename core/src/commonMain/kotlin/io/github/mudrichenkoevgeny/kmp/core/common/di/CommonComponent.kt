package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParserBuilder
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.CommonErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient.HttpClientConfigPlugin
import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.ExternalLauncher
import io.github.mudrichenkoevgeny.kmp.core.common.repository.platform.PlatformRepository
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.CommonStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.ktor.client.HttpClient
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
 * - [ClientDeviceInfo]: device identity for repositories and WebSocket bootstrap.
 * - `baseUrl`: HTTP endpoint base.
 * - `webSocketPath`: relative endpoint URL path for real-time WebSocket communication.
 * - [HttpClientConfigPlugin] list: optional extensions to the shared Ktor HTTP client (empty by default).
 * - [AccessTokenProvider]: token for authenticated HTTP and WebSocket.
 * - [CoroutineScope]: application scope for long-running SDK work (for example WebSockets).
 * - `platformContext`: optional platform handle for abstractions such as [ExternalLauncher]; may be null.
 * Host apps (see `sample/`) must call [init] before accessing [appErrorParser].
 */
public class CommonComponent(
    public val encryptedSettings: EncryptedSettings,
    clientDeviceInfo: ClientDeviceInfo,
    public val baseUrl: String,
    webSocketPath: String,
    httpClientConfigPlugins: List<HttpClientConfigPlugin> = emptyList(),
    private val accessTokenProvider: AccessTokenProvider,
    appScope: CoroutineScope,
    platformContext: Any? = null
) {

    private var _appErrorParser: AppErrorParser? = null
    public val appErrorParser: AppErrorParser
        get() = _appErrorParser ?: throw IllegalStateException(
            "AppErrorParser not initialized! Call init() first."
        )

    private val storageModule: CommonStorageModule by lazy { CommonStorageModule(encryptedSettings) }
    public val commonStorage: CommonStorage get() = storageModule.commonStorage

    private val platformModule: CommonPlatformModule by lazy { CommonPlatformModule(platformContext) }
    public val externalLauncher: ExternalLauncher get() = platformModule.externalLauncher

    private val repositoryModule: CommonRepositoryModule by lazy {
        CommonRepositoryModule(
            clientDeviceInfo
        )
    }
    public val platformRepository: PlatformRepository get() = repositoryModule.platformRepository

    private val networkModule: CommonNetworkModule by lazy {
        CommonNetworkModule(
            baseUrl = baseUrl,
            webSocketPath = webSocketPath,
            httpClientConfigPlugins = httpClientConfigPlugins,
            accessTokenProvider = accessTokenProvider,
            platformRepository = platformRepository,
            appScope = appScope
        )
    }
    public val httpClient: HttpClient get() = networkModule.httpClient
    public val webSocketService: WebSocketService get() = networkModule.webSocketService
    public val commonWebSocketMessageHandler: WebSocketMessageHandler
        get() = networkModule.commonWebSocketMessageHandler

    @InternalApi
    public constructor(
        encryptedSettings: EncryptedSettings,
        clientDeviceInfo: ClientDeviceInfo,
        baseUrl: String,
        webSocketPath: String,
        accessTokenProvider: AccessTokenProvider,
        appScope: CoroutineScope,
        platformContext: Any? = null
    ) : this(
        encryptedSettings,
        clientDeviceInfo,
        baseUrl,
        webSocketPath,
        emptyList(),
        accessTokenProvider,
        appScope,
        platformContext
    )

    /**
     * Builds and installs the root [AppErrorParser] chain used by composition locals and UI.
     *
     * @param appErrorParserCommonParser Parser used when no feature parser handles an error (defaults to [CommonErrorParser]).
     * @param appErrorParserSpecificParsers Feature parsers consulted before the common parser.
     */
    public fun init(
        appErrorParserCommonParser: AppErrorParser = CommonErrorParser,
        appErrorParserSpecificParsers: List<AppErrorParser> = emptyList()
    ) {
        _appErrorParser = AppErrorParserBuilder.build(
            commonParser = appErrorParserCommonParser,
            specificParsers = appErrorParserSpecificParsers
        )
    }
}
