package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.ManagementSettingsDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.root.ManagementSettingsRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main.MainScreenComponent
import kotlinx.browser.window
import org.w3c.dom.events.Event
import kotlin.js.ExperimentalWasmJsInterop

private const val PATH_HOME = "/home"
private const val PATH_PROFILE = "/profile"
private const val PATH_TOTP = "/totp"
private const val PATH_TOTP_RECOVERY_CODES = "/totp/recovery-codes"
private const val PATH_SESSIONS = "/sessions"
private const val PATH_IDENTIFIERS = "/identifiers"
private const val PATH_SETTINGS = "/settings"
private const val PATH_SETTINGS_AUTH = "/settings/auth"
private const val PATH_SETTINGS_GLOBAL = "/settings/global"
private const val PATH_SETTINGS_SECURITY = "/settings/security"
private const val PATH_USERS = "/users"
private const val PATH_AUDIT = "/audit"
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
                is ProfileDestination.Identifiers -> PATH_IDENTIFIERS
            }
        }
        is MainScreenComponent.Child.SettingsChild -> {
            val settingsStackState by activeChild.component.stack.subscribeAsState()
            when (settingsStackState.active.configuration) {
                is ManagementSettingsDestination.Main -> PATH_SETTINGS
                is ManagementSettingsDestination.EditAuthSettings -> PATH_SETTINGS_AUTH
                is ManagementSettingsDestination.EditGlobalSettings -> PATH_SETTINGS_GLOBAL
                is ManagementSettingsDestination.EditSecuritySettings -> PATH_SETTINGS_SECURITY
                is ManagementSettingsDestination.UsersManagement -> PATH_USERS
                is ManagementSettingsDestination.AuditLogs -> PATH_AUDIT
                is ManagementSettingsDestination.GlobalSessionList -> PATH_SESSIONS
            }
        }
    }

    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = {
            when (val currentChild = mainComponent.stack.value.active.instance) {
                is MainScreenComponent.Child.ProfileChild -> {
                    when (val profileChild = currentChild.component.stack.value.active.instance) {
                        is ProfileRootComponent.Child.TotpRecoveryCodes -> profileChild.component.onBackClick()
                        is ProfileRootComponent.Child.TotpMain -> profileChild.component.onBackClick()
                        is ProfileRootComponent.Child.Sessions -> profileChild.component.onBackClick()
                        is ProfileRootComponent.Child.Identifiers -> profileChild.component.onBackClick()
                        is ProfileRootComponent.Child.Main -> { }
                    }
                }
                is MainScreenComponent.Child.SettingsChild -> {
                    when (val settingsChild = currentChild.component.stack.value.active.instance) {
                        is ManagementSettingsRootComponent.Child.EditAuthSettings -> settingsChild.component.onBackClick()
                        is ManagementSettingsRootComponent.Child.EditGlobalSettings -> settingsChild.component.onBackClick()
                        is ManagementSettingsRootComponent.Child.EditSecuritySettings -> settingsChild.component.onBackClick()
                        is ManagementSettingsRootComponent.Child.UsersManagement -> settingsChild.component.onBackClick()
                        is ManagementSettingsRootComponent.Child.AuditLogs -> settingsChild.component.onBackClick()
                        is ManagementSettingsRootComponent.Child.Sessions -> settingsChild.component.onBackClick()
                        is ManagementSettingsRootComponent.Child.Main -> { }
                    }
                }
                is MainScreenComponent.Child.HomeChild -> { }
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
