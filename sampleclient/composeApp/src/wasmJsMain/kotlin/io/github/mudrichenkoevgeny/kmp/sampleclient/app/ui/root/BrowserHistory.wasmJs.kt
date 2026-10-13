package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.navigation.deeplink.ClientUserUrlFormatter
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink.ProfileUrlFormatter
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink.ClientAppDeepLinkParser
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.MainScreenComponent
import io.ktor.http.Url
import kotlinx.browser.window
import org.w3c.dom.events.Event

private const val PATH_ROOT = "/"
private const val PATH_PROFILE_ROOT = "/profile"
private const val EVENT_POPSTATE = "popstate"

private fun isTopLevelRootPath(path: String): Boolean {
    val cleanPath = path.substringBefore('?').substringBefore('#')
    return cleanPath == PATH_ROOT || cleanPath == PATH_PROFILE_ROOT
}

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun SetupBrowserHistory(mainComponent: MainScreenComponent) {
    val profileUrlFormatter = remember { ProfileUrlFormatter() }
    val clientUserUrlFormatter = remember { ClientUserUrlFormatter() }
    val parser = remember { ClientAppDeepLinkParser() }

    val activePath = calculateActivePath(
        mainComponent = mainComponent,
        profileUrlFormatter = profileUrlFormatter,
        clientUserUrlFormatter = clientUserUrlFormatter
    )

    val historyStack = remember {
        mutableListOf<String>().apply {
            val initialPath = window.location.pathname + window.location.search
            add(initialPath)
            window.history.replaceState(null, "", initialPath)
        }
    }

    var isProcessingPopState by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = {
            isProcessingPopState = true
            try {
                val currentPath = window.location.pathname + window.location.search
                val route = parser.parse(Url(window.location.origin + currentPath))
                if (route != null) {
                    mainComponent.handleDeepLink(route)
                } else {
                    mainComponent.onTabClick(MainScreenComponent.Config.Home)
                }

                if (historyStack.size >= 2 && currentPath == historyStack[historyStack.lastIndex - 1]) {
                    historyStack.removeAt(historyStack.lastIndex)
                } else if (historyStack.lastOrNull() != currentPath) {
                    historyStack.add(currentPath)
                }
            } finally {
                isProcessingPopState = false
            }
        }
        window.addEventListener(EVENT_POPSTATE, listener)
        onDispose {
            window.removeEventListener(EVENT_POPSTATE, listener)
        }
    }

    LaunchedEffect(activePath) {
        if (isProcessingPopState) return@LaunchedEffect
        val currentPath = window.location.pathname + window.location.search
        if (currentPath == activePath) return@LaunchedEffect

        if (historyStack.size >= 2 && activePath == historyStack[historyStack.lastIndex - 1]) {
            historyStack.removeAt(historyStack.lastIndex)
            window.history.back()
        } else if (historyStack.size == 1 && historyStack[0].startsWith(activePath.substringBefore('?'))) {
            historyStack[0] = activePath
            window.history.replaceState(null, "", activePath)
        } else if (isTopLevelRootPath(activePath)) {
            historyStack.clear()
            historyStack.add(activePath)
            window.history.replaceState(null, "", activePath)
        } else {
            if (historyStack.lastOrNull() != activePath) {
                historyStack.add(activePath)
            }
            window.history.pushState(null, "", activePath)
        }
    }
}

@Composable
private fun calculateActivePath(
    mainComponent: MainScreenComponent,
    profileUrlFormatter: ProfileUrlFormatter,
    clientUserUrlFormatter: ClientUserUrlFormatter
): String {
    val loginSlotState by mainComponent.loginDialogSlot.subscribeAsState()
    val loginChild = loginSlotState.child
    if (loginChild != null) {
        val loginStackState by loginChild.instance.stack.subscribeAsState()
        val loginActiveChild = loginStackState.active.instance
        val rawUrl = when (loginActiveChild) {
            is ClientLoginRootComponent.Child.LoginByPhone -> {
                loginActiveChild.component.stack.subscribeAsState()
                clientUserUrlFormatter.formatChild(
                    loginStackState.active.configuration,
                    loginActiveChild
                )
            }
            is ClientLoginRootComponent.Child.RegistrationByEmail -> {
                loginActiveChild.component.stack.subscribeAsState()
                clientUserUrlFormatter.formatChild(
                    loginStackState.active.configuration,
                    loginActiveChild
                )
            }
            is ClientLoginRootComponent.Child.ResetEmailPassword -> {
                loginActiveChild.component.stack.subscribeAsState()
                clientUserUrlFormatter.formatChild(
                    loginStackState.active.configuration,
                    loginActiveChild
                )
            }
            else -> clientUserUrlFormatter.format(loginStackState.active.configuration)
        }
        val absoluteUrl = Url(rawUrl)
        val query = if (absoluteUrl.encodedQuery.isEmpty()) "" else "?${absoluteUrl.encodedQuery}"
        return absoluteUrl.encodedPath + query
    }

    val stackState by mainComponent.stack.subscribeAsState()
    return when (val activeChild = stackState.active.instance) {
        is MainScreenComponent.Child.HomeChild -> PATH_ROOT
        is MainScreenComponent.Child.ProfileChild -> {
            val profileStackState by activeChild.component.stack.subscribeAsState()
            val profileActiveChild = profileStackState.active.instance
            val rawUrl = when (profileActiveChild) {
                is ProfileRootComponent.Child.TotpMain -> {
                    profileActiveChild.component.stack.subscribeAsState()
                    profileUrlFormatter.formatChild(
                        profileStackState.active.configuration,
                        profileActiveChild
                    )
                }
                is ProfileRootComponent.Child.Identifiers -> {
                    profileActiveChild.component.addIdentifierDialogSlot.subscribeAsState()
                    profileUrlFormatter.formatChild(
                        profileStackState.active.configuration,
                        profileActiveChild
                    )
                }
                else -> profileUrlFormatter.format(profileStackState.active.configuration)
            }
            val absoluteUrl = Url(rawUrl)
            val query = if (absoluteUrl.encodedQuery.isEmpty()) "" else "?${absoluteUrl.encodedQuery}"
            absoluteUrl.encodedPath + query
        }
    }
}
