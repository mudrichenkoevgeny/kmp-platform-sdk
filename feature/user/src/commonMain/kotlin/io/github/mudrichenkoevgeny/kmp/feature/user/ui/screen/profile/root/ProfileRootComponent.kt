package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.SelfIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SelfSessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SessionListOwner
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery.TotpRecoveryCodesComponent

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
        /** TOTP main screen. */
        class TotpMain(val component: TotpMainComponent) : Child
        /** TOTP recovery codes management. */
        class TotpRecoveryCodes(val component: TotpRecoveryCodesComponent) : Child
        /** Active session management. */
        class Sessions(val component: SelfSessionListComponent) : Child, SessionListOwner by component
        /** Active session detail screen. */
        class SessionDetail(val component: SessionDetailComponent) : Child
        /** Account identifiers management. */
        class Identifiers(val component: SelfIdentifierListComponent) : Child
    }
}
