package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.ManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root.ManagementRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main.MainScreenComponent
import kotlinx.browser.window
import org.w3c.dom.events.Event

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
                is ProfileDestination.SessionDetail -> PATH_SESSIONS
                is ProfileDestination.Identifiers -> PATH_IDENTIFIERS
                is ProfileDestination.IdentifierDetail -> PATH_IDENTIFIERS
            }
        }
        is MainScreenComponent.Child.SettingsChild -> {
            val settingsStackState by activeChild.component.stack.subscribeAsState()
            when (settingsStackState.active.configuration) {
                is ManagementDestination.Main -> PATH_SETTINGS
                is ManagementDestination.EditAuthSettings -> PATH_SETTINGS_AUTH
                is ManagementDestination.EditGlobalSettings -> PATH_SETTINGS_GLOBAL
                is ManagementDestination.EditSecuritySettings -> PATH_SETTINGS_SECURITY
                is ManagementDestination.GlobalUserList -> PATH_USERS
                is ManagementDestination.CreateUser -> PATH_USERS
                is ManagementDestination.UserDetail -> PATH_USERS
                is ManagementDestination.UserSessionList -> PATH_SESSIONS
                is ManagementDestination.UserIdentifierList -> PATH_IDENTIFIERS
                is ManagementDestination.AuditEventList -> PATH_AUDIT
                is ManagementDestination.AuditEventDetail -> PATH_AUDIT
                is ManagementDestination.GlobalSessionList -> PATH_SESSIONS
                is ManagementDestination.SessionDetail -> PATH_SESSIONS
                is ManagementDestination.GlobalIdentifierList -> PATH_IDENTIFIERS
                is ManagementDestination.IdentifierDetail -> PATH_IDENTIFIERS
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
                        is ProfileRootComponent.Child.SessionDetail -> profileChild.component.onBackClick()
                        is ProfileRootComponent.Child.Identifiers -> profileChild.component.onBackClick()
                        is ProfileRootComponent.Child.IdentifierDetail -> profileChild.component.onBackClick()
                        is ProfileRootComponent.Child.Main -> { }
                    }
                }
                is MainScreenComponent.Child.SettingsChild -> {
                    when (val settingsChild = currentChild.component.stack.value.active.instance) {
                        is ManagementRootComponent.Child.EditAuthSettings -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.EditGlobalSettings -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.EditSecuritySettings -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.GlobalUserList -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.CreateUser -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.UserDetail -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.UserSessionList -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.UserIdentifierList -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.AuditEventList -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.AuditEventDetail -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.GlobalSessionList -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.SessionDetail -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.GlobalIdentifierList -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.IdentifierDetail -> settingsChild.component.onBackClick()
                        is ManagementRootComponent.Child.Main -> { }
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