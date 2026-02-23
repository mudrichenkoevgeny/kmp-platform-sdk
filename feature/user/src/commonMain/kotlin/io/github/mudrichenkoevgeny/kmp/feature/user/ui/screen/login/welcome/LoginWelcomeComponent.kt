package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.welcome

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider

interface LoginWelcomeComponent {
    val state: Value<LoginWelcomeScreenState>

    fun onRetryInitClick()
    fun onLoginClick(authProvider: UserAuthProvider)
    fun onPrivacyPolicyClick()
    fun onTermsOfServiceClick()
}