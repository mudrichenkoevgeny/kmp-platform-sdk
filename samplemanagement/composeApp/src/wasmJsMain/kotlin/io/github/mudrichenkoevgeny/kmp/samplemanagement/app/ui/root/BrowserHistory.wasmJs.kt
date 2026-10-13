package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink.ManagementUrlFormatter
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root.ManagementRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink.ProfileUrlFormatter
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink.ManagementAppDeepLinkParser
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main.MainScreenComponent
import io.ktor.http.Url
import kotlinx.browser.window
import org.w3c.dom.events.Event

private const val PATH_ROOT = "/"
private const val PATH_PROFILE_ROOT = "/profile"
private const val PATH_MANAGEMENT_ROOT = "/management"
private const val PATH_SETTINGS_ROOT = "/management/settings"
private const val EVENT_POPSTATE = "popstate"

private fun isTopLevelRootPath(path: String): Boolean {
    val cleanPath = path.substringBefore('?').substringBefore('#')
    return cleanPath == PATH_ROOT ||
            cleanPath == PATH_PROFILE_ROOT ||
            cleanPath == PATH_MANAGEMENT_ROOT ||
            cleanPath == PATH_SETTINGS_ROOT
}

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun SetupBrowserHistory(mainComponent: MainScreenComponent) {
    val profileUrlFormatter = remember { ProfileUrlFormatter() }
    val managementUrlFormatter = remember { ManagementUrlFormatter() }
    val parser = remember { ManagementAppDeepLinkParser() }

    val activePath = calculateActivePath(
        mainComponent = mainComponent,
        profileUrlFormatter = profileUrlFormatter,
        managementUrlFormatter = managementUrlFormatter
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
    managementUrlFormatter: ManagementUrlFormatter
): String {
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
        is MainScreenComponent.Child.SettingsChild -> {
            val settingsStackState by activeChild.component.stack.subscribeAsState()
            val settingsActiveChild = settingsStackState.active.instance
            val rawUrl = when (settingsActiveChild) {
                is ManagementRootComponent.Child.EditAuthSettings -> {
                    settingsActiveChild.component.dialogSlot.subscribeAsState()
                    managementUrlFormatter.formatChild(
                        settingsStackState.active.configuration,
                        settingsActiveChild
                    )
                }
                is ManagementRootComponent.Child.EditGlobalSettings -> {
                    settingsActiveChild.component.dialogSlot.subscribeAsState()
                    managementUrlFormatter.formatChild(
                        settingsStackState.active.configuration,
                        settingsActiveChild
                    )
                }
                is ManagementRootComponent.Child.EditSecuritySettings -> {
                    settingsActiveChild.component.dialogSlot.subscribeAsState()
                    managementUrlFormatter.formatChild(
                        settingsStackState.active.configuration,
                        settingsActiveChild
                    )
                }
                else -> managementUrlFormatter.format(settingsStackState.active.configuration)
            }
            val absoluteUrl = Url(rawUrl)
            val query = if (absoluteUrl.encodedQuery.isEmpty()) "" else "?${absoluteUrl.encodedQuery}"
            absoluteUrl.encodedPath + query
        }
    }
}
