package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail.IdentifierDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListOwner
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.SelfIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail.SessionDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SelfSessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListOwner
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpMainComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.recovery.TotpRecoveryCodesComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserComponent
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation

/**
 * Root of the user management flow: manages navigation between main user screen
 * and detailed settings like sessions or security.
 */
interface UserRootComponent {
    /** Current navigation stack and active child. */
    val stack: Value<ChildStack<ClientUserNavigation, Child>>

    fun navigateTo(destination: ClientUserNavigation)

    /** Active child for a [ClientUserNavigation] configuration. */
    sealed interface Child {
        /** Main user dashboard screen. */
        class Main(val component: MainUserComponent) : Child
        /** TOTP main screen. */
        class TotpMain(val component: TotpMainComponent) : Child
        /** TOTP recovery codes management. */
        class TotpRecoveryCodes(val component: TotpRecoveryCodesComponent) : Child
        /** Active session management. */
        class Sessions(val component: SelfSessionListComponent) : Child, SessionListOwner by component
        /** Active session detail screen. */
        class SessionDetail(val component: SessionDetailComponent) : Child
        /** Account identifiers management. */
        class Identifiers(val component: SelfIdentifierListComponent) : Child, IdentifierListOwner by component
        /** Identifier detail screen. */
        class IdentifierDetail(val component: IdentifierDetailComponent) : Child
    }
}
