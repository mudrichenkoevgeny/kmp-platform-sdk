package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.rememberComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root.ManagementRootScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.mfa.MfaChallengeDialog
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root.UserRootScreen
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.LocalManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.mock.di.managementAppComponentMock
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreen(screenComponent: MainScreenComponent) {
    val appComponent = LocalManagementAppComponent.current

    val screenStackState by screenComponent.stack.subscribeAsState()
    val isAuthorized by screenComponent.isAuthorized.subscribeAsState()
    val loginDialogSlot by screenComponent.loginDialogSlot.subscribeAsState()
    val mfaChallengeRequest by appComponent.mfaChallengeHandler.challengeRequest.collectAsState()

    val currentNavigation = remember(screenStackState.active.configuration) {
        MainScreenDestination.fromConfig(screenStackState.active.configuration)
    }

    val destinations = remember(isAuthorized) {
        MainScreenDestination.getDestinations(isAuthorized)
    }

    val loginChild = loginDialogSlot.child
    val isStrictLogin = loginChild != null && !isAuthorized

    if (isStrictLogin) {
        ManagementLoginRootScreen(
            component = loginChild.instance,
            isDismissible = false,
            isFullScreen = true
        )
    } else {
        Box(Modifier.fillMaxSize()) {
            MainContent(
                isMobile = appComponent.commonComponent.platformRepository.getClientDeviceInfo().isMobileClient(),
                screenStack = screenComponent.stack,
                currentDestination = currentNavigation,
                destinations = destinations,
                onDestinationChange = { navItem ->
                    screenComponent.onTabClick(navItem.config)
                }
            )

            loginChild?.let { child ->
                val isDismissible = (child.configuration as? MainScreenComponent.DialogConfig.Login)?.isDismissible ?: true
                ManagementLoginRootScreen(
                    component = child.instance,
                    isDismissible = isDismissible,
                    isFullScreen = false
                )
            }

            mfaChallengeRequest?.let { request ->
                MfaChallengeDialog(
                    request = request,
                    onConfirm = appComponent.mfaChallengeHandler::onConfirm,
                    onCancel = appComponent.mfaChallengeHandler::onCancel
                )
            }
        }
    }
}

@Composable
fun MainContent(
    isMobile: Boolean,
    screenStack: Value<ChildStack<MainScreenComponent.Config, MainScreenComponent.Child>>,
    currentDestination: MainScreenDestination,
    destinations: List<MainScreenDestination>,
    onDestinationChange: (MainScreenDestination) -> Unit
) {
    val content: @Composable () -> Unit = {
        Children(
            stack = screenStack,
            animation = stackAnimation(fade())
        ) { child ->
            when (val instance = child.instance) {
                is MainScreenComponent.Child.UserChild -> {
                    UserRootScreen(instance.component)
                }
                is MainScreenComponent.Child.ManagementChild -> {
                    ManagementRootScreen(instance.component)
                }
            }
        }
    }

    if (isMobile) {
        MobileLayout(
            currentDestination = currentDestination,
            destinations = destinations,
            onDestinationChange = onDestinationChange,
            content = content
        )
    } else {
        WebLayout(
            currentDestination = currentDestination,
            destinations = destinations,
            onDestinationChange = onDestinationChange,
            content = content
        )
    }
}

@Composable
private fun MobileLayout(
    currentDestination: MainScreenDestination,
    destinations: List<MainScreenDestination>,
    onDestinationChange: (MainScreenDestination) -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                destinations.forEach { dest ->
                    NavigationBarItem(
                        selected = currentDestination == dest,
                        onClick = { onDestinationChange(dest) },
                        icon = {
                            Icon(
                                painter = painterResource(dest.iconRes),
                                contentDescription = null,
                                modifier = Modifier.size(CoreTheme.dimens.progressIndicatorSizeSmall)
                            )
                        },
                        label = { Text(dest.title()) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            content()
        }
    }
}

@Composable
private fun WebLayout(
    currentDestination: MainScreenDestination,
    destinations: List<MainScreenDestination>,
    onDestinationChange: (MainScreenDestination) -> Unit,
    content: @Composable () -> Unit
) {
    Row(Modifier.fillMaxSize()) {
        NavigationRail(
            modifier = Modifier.width(CoreTheme.dimens.navigationRailWidth),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Spacer(Modifier.height(CoreTheme.dimens.paddingLarge))
            destinations.forEach { dest ->
                NavigationRailItem(
                    selected = currentDestination == dest,
                    onClick = { onDestinationChange(dest) },
                    icon = {
                        Icon(
                            painter = painterResource(dest.iconRes),
                            contentDescription = dest.title(),
                            modifier = Modifier.size(CoreTheme.dimens.progressIndicatorSizeSmall)
                        )
                    },
                    label = { Text(dest.title()) }
                )
            }
        }

        Box(Modifier.weight(1f).fillMaxHeight()) {
            content()
        }
    }
}

@InternalApi
@Composable
private fun MainScreenPreviewContent() {
    val appComponent = remember { managementAppComponentMock() }
    val componentContext = rememberComponentContext()
    val mainComponent = remember {
        appComponent.createMainScreenComponent(componentContext = componentContext)
    }

    CompositionLocalProvider(
        LocalManagementAppComponent provides appComponent,
        LocalCommonComponent provides appComponent.commonComponent,
        LocalErrorParser provides AppErrorParserMock
    ) {
        MainScreen(screenComponent = mainComponent)
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    ScreenPreviewContainer {
        MainScreenPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        MainScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        MainScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        MainScreenPreviewContent()
    }
}
