package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.sample.app.di.LocalAppComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.home.HomeScreen
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile.ProfileScreen
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.root.LoginRootScreen
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.home.HomeScreenComponent
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(screenComponent: MainScreenComponent) {
    val appComponent = LocalAppComponent.current

    val screenStackState by screenComponent.stack.subscribeAsState()
    val loginDialogSlot by screenComponent.loginDialogSlot.subscribeAsState()

    val currentNavigation = remember(screenStackState.active.configuration) {
        MainScreenDestination.fromConfig(screenStackState.active.configuration)
    }

    Box(Modifier.fillMaxSize()) {
        MainContent(
            isMobile = appComponent.commonComponent.deviceInfo.isMobileClient(),
            screenStack = screenComponent.stack,
            currentDestination = currentNavigation,
            destinations = MainScreenDestination.allDestinations,
            onDestinationChange = { navItem ->
                screenComponent.onTabClick(navItem.config)
            }
        )

        loginDialogSlot.child?.instance?.let { component ->
            LoginRootScreen(component = component)
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
                    ProfileScreen(instance.component)
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
    onDestinationChange: (MainScreenDestination) -> Unit,
    content: @Composable () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Surface(shadowElevation = Dimens.shadowElevation) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(Dimens.rowHeight)
                    .padding(horizontal = Dimens.paddingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { onDestinationChange(MainScreenDestination.Home) }) {
                    Text(
                        text = stringResource(MainScreenDestination.Home.title),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                IconButton(onClick = { onDestinationChange(MainScreenDestination.Profile) }) {
                    Icon(
                        imageVector = MainScreenDestination.Profile.icon,
                        contentDescription = stringResource(MainScreenDestination.Profile.title),
                        tint = if (currentDestination == MainScreenDestination.Profile)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Box(Modifier.fillMaxSize()) {
            content()
        }
    }
}

private val mockStack = MutableValue(
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

@Preview
@Composable
private fun MobileMainScreenPreview() {
    MaterialTheme {
        MainContent(
            isMobile = true,
            screenStack = mockStack,
            currentDestination = MainScreenDestination.Home,
            destinations = MainScreenDestination.allDestinations,
            onDestinationChange = { }
        )
    }
}

@Preview
@Composable
private fun WebMainScreenPreview() {
    MaterialTheme {
        MainContent(
            isMobile = false,
            screenStack = mockStack,
            currentDestination = MainScreenDestination.Home,
            destinations = MainScreenDestination.allDestinations,
            onDestinationChange = { }
        )
    }
}