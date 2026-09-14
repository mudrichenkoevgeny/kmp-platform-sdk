package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.ManagementSettingsRootScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileRootScreen
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.LocalManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.home.HomeScreen
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.home.HomeScreenComponent
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(screenComponent: MainScreenComponent) {
    val appComponent = LocalManagementAppComponent.current

    val screenStackState by screenComponent.stack.subscribeAsState()
    val loginDialogSlot by screenComponent.loginDialogSlot.subscribeAsState()

    val currentNavigation = remember(screenStackState.active.configuration) {
        MainScreenDestination.fromConfig(screenStackState.active.configuration)
    }

    Box(Modifier.fillMaxSize()) {
        MainContent(
            isMobile = appComponent.commonComponent.platformRepository.getDeviceInfo().isMobileClient(),
            screenStack = screenComponent.stack,
            currentDestination = currentNavigation,
            destinations = MainScreenDestination.allDestinations,
            onDestinationChange = { navItem ->
                screenComponent.onTabClick(navItem.config)
            }
        )

        loginDialogSlot.child?.instance?.let { component ->
            ManagementLoginRootScreen(component = component)
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
                is MainScreenComponent.Child.HomeChild -> {
                    HomeScreen(instance.component)
                }
                is MainScreenComponent.Child.ProfileChild -> {
                    ProfileRootScreen(instance.component)
                }
                is MainScreenComponent.Child.SettingsChild -> {
                    ManagementSettingsRootScreen(instance.component)
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
                        icon = { Icon(dest.icon, contentDescription = null) },
                        label = { Text(stringResource(dest.title)) }
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
    Column(Modifier.fillMaxSize()) {
        Surface(shadowElevation = CoreTheme.dimens.shadowElevation) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(CoreTheme.dimens.rowHeight)
                    .padding(horizontal = CoreTheme.dimens.paddingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { onDestinationChange(MainScreenDestination.Home) }) {
                    Text(
                        text = stringResource(MainScreenDestination.Home.title),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
                ) {
                    destinations.filter { it != MainScreenDestination.Home }.forEach { dest ->
                        IconButton(onClick = { onDestinationChange(dest) }) {
                            Icon(
                                imageVector = dest.icon,
                                contentDescription = stringResource(dest.title),
                                tint = if (currentDestination == dest)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Box(Modifier.fillMaxSize()) {
            content()
        }
    }
}

private val screenStackMock = MutableValue(
    ChildStack(
        active = Child.Created(
            configuration = MainScreenComponent.Config.Home,
            instance = MainScreenComponent.Child.HomeChild(
                object : HomeScreenComponent {}
            )
        ),
        backStack = emptyList()
    )
)

private class MainScreenPreviewProvider : PreviewParameterProvider<MainScreenDestination> {
    private val items: List<Pair<String, MainScreenDestination>> = listOf(
        "Home Tab Active" to MainScreenDestination.Home,
        "Profile Tab Active" to MainScreenDestination.Profile,
        "Settings Tab Active" to MainScreenDestination.Settings
    )

    override val values: Sequence<MainScreenDestination> =
        items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? =
        items.getOrNull(index)?.first
}

@Composable
private fun MainScreenPreviewContent(
    destination: MainScreenDestination,
    isMobile: Boolean
) {
    Surface {
        MainContent(
            isMobile = isMobile,
            screenStack = screenStackMock,
            currentDestination = destination,
            destinations = MainScreenDestination.allDestinations,
            onDestinationChange = { }
        )
    }
}

@Preview(showBackground = true, group = "States")
@Composable
private fun MainScreenStatesPreview(
    @PreviewParameter(MainScreenPreviewProvider::class) destination: MainScreenDestination
) {
    ScreenPreviewContainer { isMobile ->
        MainScreenPreviewContent(
            destination = destination,
            isMobile = isMobile
        )
    }
}

@ScreenSizePreviews
@Composable
private fun MainScreenAdaptivePreview() {
    ScreenPreviewContainer { isMobile ->
        MainScreenPreviewContent(
            destination = MainScreenDestination.Home,
            isMobile = isMobile
        )
    }
}

@ThemePreviews
@Composable
private fun MainScreenThemePreview() {
    ScreenPreviewContainer { isMobile ->
        MainScreenPreviewContent(
            destination = MainScreenDestination.Home,
            isMobile = isMobile
        )
    }
}

@FontScalePreviews
@Composable
private fun MainScreenFontScalePreview() {
    ScreenPreviewContainer { isMobile ->
        MainScreenPreviewContent(
            destination = MainScreenDestination.Home,
            isMobile = isMobile
        )
    }
}