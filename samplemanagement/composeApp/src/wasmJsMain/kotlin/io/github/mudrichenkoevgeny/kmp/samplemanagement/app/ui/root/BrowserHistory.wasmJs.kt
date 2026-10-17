package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpStepConfig
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root.UserRootComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink.ManagementAppDeepLinkParser
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main.MainScreenComponent
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.toFullUrl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.totp.TotpStep
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementAuthNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.login.totp.LoginByTotpMode
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.login.totp.LoginByTotpParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.totp.main.TotpMainParams
import io.ktor.http.Url
import kotlinx.browser.window
import org.w3c.dom.events.Event

private const val PATH_USER_ROOT = "/user"
private const val PATH_SELF_USER_ROOT = "/self/user"
private const val PATH_MANAGEMENT_ROOT = "/management"
private const val EVENT_POPSTATE = "popstate"

private fun isTopLevelRootPath(path: String): Boolean {
    val cleanPath = path.substringBefore('?').substringBefore('#')
    return cleanPath == "/" ||
            cleanPath == PATH_USER_ROOT ||
            cleanPath == PATH_SELF_USER_ROOT ||
            cleanPath == PATH_MANAGEMENT_ROOT
}

private fun getAncestorPaths(fullPath: String): List<String> {
    val cleanPath = fullPath.substringBefore('?').substringBefore('#')
    val segments = cleanPath.split('/').filter { it.isNotEmpty() }
    if (segments.isEmpty()) {
        return emptyList()
    }

    val ancestors = mutableListOf<String>()
    var current = ""
    ancestors.add("/")
    for (i in 0 until segments.lastIndex) {
        current += "/" + segments[i]
        ancestors.add(current)
    }
    return ancestors
}

/**
 * Wasm/web implementation of browser history synchronization for the management portal:
 * keeps browser address bar URL, history stack, and popstate navigation events in sync
 * with active Decompose screen state.
 *
 * @param mainComponent Root navigation component driving active management screens and dialogs.
 * @param baseUrl Host domain base URL used to compute relative active path segments.
 */
@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun SetupBrowserHistory(
    mainComponent: MainScreenComponent,
    baseUrl: String
) {
    val parser = remember { ManagementAppDeepLinkParser() }

    val activePath = calculateActivePath(
        mainComponent = mainComponent,
        baseUrl = baseUrl
    )

    val historyStack = remember {
        mutableListOf<String>().apply {
            val initialPath = window.location.pathname + window.location.search
            val ancestorPaths = getAncestorPaths(initialPath)
            if (ancestorPaths.isNotEmpty()) {
                window.history.replaceState(null, "", ancestorPaths.first())
                add(ancestorPaths.first())
                for (i in 1 until ancestorPaths.size) {
                    val path = ancestorPaths[i]
                    window.history.pushState(null, "", path)
                    add(path)
                }
                window.history.pushState(null, "", initialPath)
                add(initialPath)
            } else {
                add(initialPath)
                window.history.replaceState(null, "", initialPath)
            }
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
                    mainComponent.onTabClick(MainScreenComponent.Config.Management)
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
        if (isProcessingPopState) {
            return@LaunchedEffect
        }
        val currentPath = window.location.pathname + window.location.search
        if (currentPath == activePath) {
            return@LaunchedEffect
        }

        if (historyStack.size >= 2 && activePath == historyStack[historyStack.lastIndex - 1]) {
            historyStack.removeAt(historyStack.lastIndex)
            window.history.back()
        } else if (historyStack.size == 1 && historyStack[0] == activePath) {
            window.history.replaceState(null, "", activePath)
        } else if (historyStack.size == 1 && historyStack[0].startsWith(activePath.substringBefore('?')) && activePath != "/") {
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
    baseUrl: String
): String {
    val loginSlotState by mainComponent.loginDialogSlot.subscribeAsState()
    val loginChild = loginSlotState.child
    if (loginChild != null) {
        val loginStackState by loginChild.instance.stack.subscribeAsState()
        val currentRoute = loginStackState.active.configuration
        val effectiveRoute = if (currentRoute is ManagementAuthNavigation.LoginByTotp) {
            val childInstance = loginStackState.active.instance
            if (childInstance is ManagementLoginRootComponent.Child.LoginByTotp) {
                val totpState by childInstance.component.state.subscribeAsState()
                val contentState = totpState as? LoginByTotpScreenState.Content
                val mode = if (contentState?.mode == LoginByTotpMode.RECOVERY_CODE) {
                    LoginByTotpMode.RECOVERY_CODE
                } else {
                    LoginByTotpMode.TOTP
                }
                ManagementAuthNavigation.LoginByTotp(
                    LoginByTotpParams(
                        mfaToken = contentState?.mfaToken ?: currentRoute.params.mfaToken,
                        loginByTotpMode = mode,
                        redirectUrl = currentRoute.params.redirectUrl
                    )
                )
            } else {
                currentRoute
            }
        } else {
            currentRoute
        }

        val rawUrl = effectiveRoute.toFullUrl(baseUrl)
        val absoluteUrl = Url(rawUrl)
        val path = absoluteUrl.encodedPath.ifEmpty { "/" }
        val query = if (absoluteUrl.encodedQuery.isEmpty()) "" else "?${absoluteUrl.encodedQuery}"
        return path + query
    }

    val stackState by mainComponent.stack.subscribeAsState()
    return when (val activeChild = stackState.active.instance) {
        is MainScreenComponent.Child.UserChild -> {
            val userStackState by activeChild.component.stack.subscribeAsState()
            val currentRoute = userStackState.active.configuration
            val effectiveRoute = if (currentRoute is ClientUserNavigation.TotpMain) {
                val totpChild = userStackState.active.instance
                if (totpChild is UserRootComponent.Child.TotpMain) {
                    val totpStackState by totpChild.component.stack.subscribeAsState()
                    val step = if (totpStackState.active.configuration is TotpStepConfig.Setup) TotpStep.SETUP else TotpStep.OVERVIEW
                    ClientUserNavigation.TotpMain(TotpMainParams(totpStep = step))
                } else {
                    currentRoute
                }
            } else {
                currentRoute
            }
            val rawUrl = effectiveRoute.toFullUrl(baseUrl)
            val absoluteUrl = Url(rawUrl)
            val path = absoluteUrl.encodedPath.ifEmpty { "/" }
            val query = if (absoluteUrl.encodedQuery.isEmpty()) "" else "?${absoluteUrl.encodedQuery}"
            path + query
        }
        is MainScreenComponent.Child.ManagementChild -> {
            val managementStackState by activeChild.component.stack.subscribeAsState()
            val currentRoute = managementStackState.active.configuration
            val rawUrl = currentRoute.toFullUrl(baseUrl)
            val absoluteUrl = Url(rawUrl)
            val path = absoluteUrl.encodedPath.ifEmpty { "/" }
            val query = if (absoluteUrl.encodedQuery.isEmpty()) "" else "?${absoluteUrl.encodedQuery}"
            path + query
        }
    }
}
