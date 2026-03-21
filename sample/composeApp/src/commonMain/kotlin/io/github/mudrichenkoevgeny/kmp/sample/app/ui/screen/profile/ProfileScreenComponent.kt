package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile

import com.arkivanov.decompose.value.Value

/**
 * Profile tab logic: exposes reactive [ProfileScreenState] and login dialog requests.
 */
interface ProfileScreenComponent {
    /**
     * Hot state for the profile UI.
     */
    val state: Value<ProfileScreenState>

    /**
     * Invoked when the user chooses to sign in from the unauthorized state.
     */
    fun onLoginClick()
}