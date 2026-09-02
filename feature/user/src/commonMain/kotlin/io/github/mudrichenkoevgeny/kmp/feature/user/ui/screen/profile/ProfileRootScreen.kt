package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.ProfileRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.main.MainProfileComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.IdentifierListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.SessionListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.TotpSettingsScreen

@Composable
fun ProfileRootScreen(component: ProfileRootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(slide()),
    ) { child ->
        when (val instance = child.instance) {
            is ProfileRootComponent.Child.Main -> MainProfileScreen(instance.component)
            is ProfileRootComponent.Child.TotpSettings -> TotpSettingsScreen(instance.component)
            is ProfileRootComponent.Child.Sessions -> SessionListScreen(instance.component)
            is ProfileRootComponent.Child.Identifiers -> IdentifierListScreen(instance.component)
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun ProfileRootScreenPreview() {
    val mainMock = MainProfileComponentMock(
        initialState = MainProfileScreenState.Content(user = userDetailsMock())
    )
    val rootMock = ProfileRootComponentMock(
        initialChild = ProfileRootComponent.Child.Main(mainMock)
    )

    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                ProfileRootScreen(component = rootMock)
            }
        }
    }
}
