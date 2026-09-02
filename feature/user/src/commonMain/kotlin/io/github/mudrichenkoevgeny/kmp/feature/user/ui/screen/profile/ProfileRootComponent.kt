package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.IdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.SessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.TotpSettingsComponent

/**
 * Root of the profile management flow: manages navigation between main profile
 * and detailed settings like sessions or security.
 */
interface ProfileRootComponent {
    /**
     * Current navigation stack and active child.
     */
    val stack: Value<ChildStack<ProfileDestination, Child>>

    /**
     * Active child for a [ProfileDestination] configuration.
     */
    sealed interface Child {
        /** Main profile summary screen. */
        class Main(val component: MainProfileComponent) : Child
        /** TOTP settings and recovery codes. */
        class TotpSettings(val component: TotpSettingsComponent) : Child
        /** Active session management. */
        class Sessions(val component: SessionListComponent) : Child
        /** Account identifiers management. */
        class Identifiers(val component: IdentifierListComponent) : Child
    }
}
