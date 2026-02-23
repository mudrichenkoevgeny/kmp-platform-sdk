package io.github.mudrichenkoevgeny.kmp.sample.app.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.di.EncryptedSettingsComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.model.mockDeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.security.error.parser.SecurityErrorParser
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.error.pasrer.UserErrorParser
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.UserAuthServicesMock
import io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.AuthHttpClientConfigPlugin
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.EncryptedAuthStorage
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main.MainScreenComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main.MainScreenComponentImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppComponent(
    platformContext: Any? = null,
    deviceInfo: DeviceInfo,
    baseUrl: String,
    authServices: UserAuthServices
) {

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    @InternalApi
    constructor(
        mockCommonComponent: CommonComponent,
        mockSettingsComponent: SettingsComponent,
        mockSecurityComponent: SecurityComponent,
        mockUserComponent: UserComponent
    ) : this(
        platformContext = null,
        deviceInfo = mockDeviceInfo(),
        baseUrl = "",
        authServices = UserAuthServicesMock()
    ) {
        this.mockCommonComponent = mockCommonComponent
        this.mockSettingsComponent = mockSettingsComponent
        this.mockSecurityComponent = mockSecurityComponent
        this.mockUserComponent = mockUserComponent
        _isInitialized.value = true
    }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val encryptedSettingsComponent by lazy {
        EncryptedSettingsComponent(platformContext)
    }
    private val encryptedSettings get() = encryptedSettingsComponent.encryptedSettings

    val authStorage: AuthStorage by lazy {
        EncryptedAuthStorage(
            encryptedSettings = encryptedSettings,
            scope = appScope
        )
    }

    private val authHttpClientConfigPlugin = AuthHttpClientConfigPlugin(
        baseUrl = baseUrl,
        authStorage = authStorage
    )

    private var mockCommonComponent: CommonComponent? = null
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

    private var mockSettingsComponent: SettingsComponent? = null
    val settingsComponent: SettingsComponent by lazy {
        mockSettingsComponent ?: SettingsComponent(
            encryptedSettings = encryptedSettings,
            httpClient = commonComponent.httpClient,
            webSocketService = commonComponent.webSocketService,
            parentScope = appScope
        )
    }

    private var mockSecurityComponent: SecurityComponent? = null
    val securityComponent: SecurityComponent by lazy {
        mockSecurityComponent ?: SecurityComponent(
            encryptedSettings = encryptedSettings,
            httpClient = commonComponent.httpClient,
            webSocketService = commonComponent.webSocketService,
            parentScope = appScope
        )
    }

    private var mockUserComponent: UserComponent? = null
    val userComponent: UserComponent by lazy {
        mockUserComponent ?: UserComponent(
            commonComponent = commonComponent,
            settingsComponent = settingsComponent,
            securityComponent = securityComponent,
            authStorage = authStorage,
            authServices = authServices,
            parentScope = appScope
        )
    }
    val refreshUserConfigurationUseCase get() = userComponent.refreshUserConfigurationUseCase

    private val appUseCaseModule = AppUseCaseModule(
        refreshGlobalSettingsUseCase = settingsComponent.refreshGlobalSettingsUseCase,
        refreshSecuritySettingsUseCase = securityComponent.refreshSecuritySettingsUseCase,
        refreshAuthSettingsUseCase = userComponent.refreshAuthSettingsUseCase
    )
    val syncDataUseCase get() = appUseCaseModule.syncDataUseCase

    fun init() {
        if (_isInitialized.value) {
            return
        }

        commonComponent.init(
            appErrorParserSpecificParsers = listOf(SecurityErrorParser, UserErrorParser)
        )
        _isInitialized.value = true
    }

    fun createMainScreenComponent(
        componentContext: ComponentContext
    ): MainScreenComponent {
        return MainScreenComponentImpl(
            componentContext = componentContext,
            appComponent = this
        )
    }
}