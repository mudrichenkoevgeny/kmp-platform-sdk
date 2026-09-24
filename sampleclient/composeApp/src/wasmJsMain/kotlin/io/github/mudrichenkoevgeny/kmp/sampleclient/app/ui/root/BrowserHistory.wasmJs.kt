package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.MainScreenComponent
import kotlinx.browser.window
import org.w3c.dom.events.Event

private const val PATH_HOME = "/home"
private const val PATH_PROFILE = "/profile"
private const val PATH_TOTP = "/totp"
private const val PATH_TOTP_RECOVERY_CODES = "/totp/recovery-codes"
private const val PATH_SESSIONS = "/sessions"
private const val PATH_IDENTIFIERS = "/identifiers"
private const val EVENT_POPSTATE = "popstate"

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun SetupBrowserHistory(mainComponent: MainScreenComponent) {
    val stackState by mainComponent.stack.subscribeAsState()

    val path = when (val activeChild = stackState.active.instance) {
        is MainScreenComponent.Child.HomeChild -> PATH_HOME
        is MainScreenComponent.Child.ProfileChild -> {
            val profileStackState by activeChild.component.stack.subscribeAsState()
            when (profileStackState.active.configuration) {
                is ProfileDestination.Main -> PATH_PROFILE
                is ProfileDestination.TotpMain -> PATH_TOTP
                is ProfileDestination.TotpRecoveryCodes -> PATH_TOTP_RECOVERY_CODES
                is ProfileDestination.Sessions -> PATH_SESSIONS
                is ProfileDestination.SessionDetail -> PATH_SESSIONS
                is ProfileDestination.Identifiers -> PATH_IDENTIFIERS
                is ProfileDestination.IdentifierDetail -> PATH_IDENTIFIERS
            }
        }
    }

    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = {
            val currentChild = mainComponent.stack.value.active.instance
            if (currentChild is MainScreenComponent.Child.ProfileChild) {
                when (val profileChild = currentChild.component.stack.value.active.instance) {
                    is ProfileRootComponent.Child.TotpRecoveryCodes -> profileChild.component.onBackClick()
                    is ProfileRootComponent.Child.TotpMain -> profileChild.component.onBackClick()
                    is ProfileRootComponent.Child.Sessions -> profileChild.component.onBackClick()
                    is ProfileRootComponent.Child.SessionDetail -> profileChild.component.onBackClick()
                    is ProfileRootComponent.Child.Identifiers -> profileChild.component.onBackClick()
                    is ProfileRootComponent.Child.IdentifierDetail -> profileChild.component.onBackClick()
                    is ProfileRootComponent.Child.Main -> { }
                }
            }
        }
        window.addEventListener(EVENT_POPSTATE, listener)
        onDispose {
            window.removeEventListener(EVENT_POPSTATE, listener)
        }
    }

    LaunchedEffect(path) {
        if (window.location.pathname != path) {
            window.history.pushState(null, "", path)
        }
    }
}
