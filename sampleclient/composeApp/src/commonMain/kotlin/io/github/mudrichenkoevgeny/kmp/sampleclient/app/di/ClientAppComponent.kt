package io.github.mudrichenkoevgeny.kmp.sampleclient.app.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.di.EncryptedSettingsComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.model.deviceInfoMock
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.security.error.parser.SecurityErrorParser
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.di.ClientUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.securityapi.di.SecurityApiComponent
import io.github.mudrichenkoevgeny.kmp.feature.settingsapi.di.SettingsApiComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.error.parser.UserErrorParser
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.UserAuthServicesMock
import io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.AuthHttpClientConfigPlugin
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.EncryptedAuthStorage
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.MainScreenComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.ClientMainScreenComponentImpl
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Sample host root: builds encrypted storage, [CommonComponent], [SettingsComponent],
 * [SecurityComponent], then exposes refresh and sync entry points used at startup.
 *
 * Production-style constructor:
 * - `platformContext`: optional handle for platform services (Android `Application`, and similar).
 * - [ClientDeviceInfo]: device identity for networking and WebSocket bootstrap.
 * - `baseUrl`: HTTP and WebSocket base URL for the sample backend.
 * - [UserAuthServices]: platform auth integrations (Google sign-in, and similar).
 *
 * Call [init] once before relying on [CommonComponent.appErrorParser] or socket handlers. [init] registers
 * feature error parsers on the common component and installs WebSocket message handlers from each module.
 *
 * The internal `@InternalApi` constructor is used by the mock factory under `mock.di` in tests and previews; it skips
 * real encrypted settings construction and marks the app as initialized immediately.
 */
class ClientAppComponent(
    platformContext: Any? = null,
    deviceInfo: ClientDeviceInfo,
    baseUrl: String,
    authServices: UserAuthServices
) {

    private val _isInitialized = MutableStateFlow(false)

    /**
     * Emits `true` once [init] has been successfully completed.
     */
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    @InternalApi
    constructor(
        platformContext: Any?,
        mockCommonComponent: CommonComponent,
        mockSettingsComponent: SettingsComponent,
        mockSecurityComponent: SecurityComponent,
        mockClientUserComponent: ClientUserComponent
    ) : this(
        platformContext = platformContext,
        deviceInfo = deviceInfoMock(),
        baseUrl = "",
        authServices = UserAuthServicesMock()
    ) {
        this.mockCommonComponent = mockCommonComponent
        this.mockSettingsComponent = mockSettingsComponent
        this.mockSecurityComponent = mockSecurityComponent
        this.mockClientUserComponent = mockClientUserComponent
        _isInitialized.value = true
    }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val encryptedSettingsComponent by lazy {
        EncryptedSettingsComponent(platformContext)
    }
    private val encryptedSettings get() = encryptedSettingsComponent.encryptedSettings

    /**
     * Persistence for session tokens.
     */
    val authStorage: AuthStorage by lazy {
        EncryptedAuthStorage(
            encryptedSettings = encryptedSettings,
            scope = appScope
        )
    }

    /**
     * Must be lazy: the [InternalApi] mock constructor assigns [mockCommonComponent] (and related mocks)
     * after the primary constructor runs; eager init would force [authStorage] / encrypted settings too early.
     */
    private val authHttpClientConfigPlugin by lazy {
        AuthHttpClientConfigPlugin(
            baseUrl = baseUrl,
            authStorage = authStorage
        )
    }

    private var mockCommonComponent: CommonComponent? = null

    /**
     * Shared networking and base platform infrastructure.
     */
    val commonComponent: CommonComponent by lazy {
        mockCommonComponent ?: CommonComponent(
            encryptedSettings = encryptedSettings,
            deviceInfo = deviceInfo,
            baseUrl = baseUrl,
            httpClientConfigPlugins = listOf(authHttpClientConfigPlugin),
            accessTokenProvider = authStorage,
            appScope = appScope,
            platformContext = platformContext
        )
    }

    private val settingsApiComponent by lazy {
        SettingsApiComponent(
            httpClient = commonComponent.httpClient
        )
    }

    /** REST API for global settings. */
    val globalSettingsApi = settingsApiComponent.globalSettingsApi

    private var mockSettingsComponent: SettingsComponent? = null

    /** Domain logic for global app settings. */
    val settingsComponent: SettingsComponent by lazy {
        mockSettingsComponent ?: SettingsComponent(
            webSocketService = commonComponent.webSocketService,
            globalSettingsApi = globalSettingsApi,
            encryptedSettings = encryptedSettings,
            parentScope = appScope
        )
    }

    private val securityApiComponent by lazy {
        SecurityApiComponent(
            httpClient = commonComponent.httpClient
        )
    }

    /** REST API for security metadata. */
    val securitySettingsApi = securityApiComponent.securitySettingsApi

    private var mockSecurityComponent: SecurityComponent? = null

    /** Domain logic for password policies and security states. */
    val securityComponent: SecurityComponent by lazy {
        mockSecurityComponent ?: SecurityComponent(
            webSocketService = commonComponent.webSocketService,
            securitySettingsApi = securitySettingsApi,
            encryptedSettings = encryptedSettings,
            parentScope = appScope
        )
    }

    private var mockClientUserComponent: ClientUserComponent? = null

    /** Root component for client-user specific logic and UI. */
    val clientUserComponent: ClientUserComponent by lazy {
        mockClientUserComponent ?: ClientUserComponent(
            commonComponent = commonComponent,
            settingsComponent = settingsComponent,
            securityComponent = securityComponent,
            authStorage = authStorage,
            authServices = authServices,
            parentScope = appScope
        )
    }

    /** Atomic use case for refreshing full user state. */
    val refreshUserConfigurationUseCase get() = clientUserComponent.refreshUserConfigurationUseCase

    /**
     * Must be lazy for the same reason as [authHttpClientConfigPlugin] — otherwise [settingsComponent] /
     * [clientUserComponent] are touched before mock fields are assigned.
     */
    private val clientAppUseCaseModule by lazy {
        ClientAppUseCaseModule(
            refreshGlobalSettingsUseCase = settingsComponent.refreshGlobalSettingsUseCase,
            refreshSecuritySettingsUseCase = securityComponent.refreshSecuritySettingsUseCase,
            refreshAuthSettingsUseCase = clientUserComponent.refreshAuthSettingsUseCase
        )
    }

    /** Concurrent sync operation for all settings modules. */
    val syncDataUseCase get() = clientAppUseCaseModule.syncDataUseCase

    /**
     * Registers feature error parsers and the combined WebSocket handler list on the shared socket service,
     * then sets [isInitialized] to `true`. Safe to call once; subsequent calls are no-ops.
     */
    fun init() {
        if (_isInitialized.value) {
            return
        }

        commonComponent.init(
            appErrorParserSpecificParsers = listOf(SecurityErrorParser, UserErrorParser)
        )
        val handlers = listOf(
            commonComponent.commonWebSocketMessageHandler,
            settingsComponent.settingsWebSocketMessageHandler,
            securityComponent.securityWebSocketMessageHandler,
            clientUserComponent.userWebSocketMessageHandler
        )
        commonComponent.webSocketService.updateWebSocketMessageHandlers(handlers)
        _isInitialized.value = true
    }

    /**
     * @param componentContext Decompose context for the tab stack and dialog slot.
     * @return [MainScreenComponent] hosting home and profile destinations.
     */
    fun createMainScreenComponent(
        componentContext: ComponentContext
    ): MainScreenComponent {
        return ClientMainScreenComponentImpl(
            componentContext = componentContext,
            clientAppComponent = this
        )
    }
}