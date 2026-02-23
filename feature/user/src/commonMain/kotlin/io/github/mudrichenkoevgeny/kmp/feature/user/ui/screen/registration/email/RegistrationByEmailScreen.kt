package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.registration.email

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun RegistrationByEmailScreen(component: RegistrationByEmailComponent) {
    val state by component.state.subscribeAsState()
}