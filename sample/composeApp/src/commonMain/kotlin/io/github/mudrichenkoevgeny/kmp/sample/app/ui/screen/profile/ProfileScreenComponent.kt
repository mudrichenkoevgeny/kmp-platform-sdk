package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile

import com.arkivanov.decompose.value.Value

interface ProfileScreenComponent {
    val state: Value<ProfileScreenState>
    fun onLoginClick()
}