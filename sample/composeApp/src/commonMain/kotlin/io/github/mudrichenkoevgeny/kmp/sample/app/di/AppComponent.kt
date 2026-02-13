package io.github.mudrichenkoevgeny.kmp.sample.app.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.config.model.CommonConfig
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserComponent
import io.github.mudrichenkoevgeny.kmp.sample.BuildConfig
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main.MainScreenComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main.MainScreenComponentImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppComponent(
    platformContext: Any? = null
) {
    @InternalApi
    constructor(
        mockCommonComponent: CommonComponent
    ) : this(null) {
        this.mockCommonComponent = mockCommonComponent
        _isInitialized.value = true
    }

    private var mockCommonComponent: CommonComponent? = null

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val commonConfig = CommonConfig(
        baseUrl = BuildConfig.BASE_URL,
        appVersion = BuildConfig.APP_VERSION
    )

    val commonComponent: CommonComponent by lazy {
        mockCommonComponent ?: CommonComponent(
            commonConfig = commonConfig,
            platformContext = platformContext
        )
    }

    val userComponent by lazy {
        UserComponent(
            commonComponent = commonComponent,
            baseUrl = BuildConfig.BASE_URL
        )
    }

    fun createMainScreenComponent(
        componentContext: ComponentContext
    ): MainScreenComponent {
        return MainScreenComponentImpl(
            componentContext = componentContext,
            appComponent = this
        )
    }

    suspend fun init() {
        if (_isInitialized.value) {
            return
        }

        commonComponent.init()

        _isInitialized.value = true
    }
}