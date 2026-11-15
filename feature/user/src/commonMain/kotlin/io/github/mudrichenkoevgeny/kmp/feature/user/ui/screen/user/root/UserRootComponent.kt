package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.StorageFileDetailComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload.StorageFileUploadScreenComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit.UserEditComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail.IdentifierDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.IdentifierListOwner
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail.SessionDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListOwner
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpMainComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.recovery.TotpRecoveryCodesComponent
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.NavigationRoute

/**
 * Root of the user management flow: manages navigation between main user screen
 * and detailed settings like sessions or security.
 */
public interface UserRootComponent {
    /** Current navigation stack and active child. */
    public val stack: Value<ChildStack<NavigationRoute, Child>>

    public fun navigateTo(destination: NavigationRoute)

    /** Active child for a [NavigationRoute] configuration. */
    public sealed interface Child {
        /** Main user dashboard screen. */
        public class Main(public val component: MainUserComponent) : Child

        /** User edit screen. */
        public class UserEdit(public val component: UserEditComponent) : Child

        /** TOTP main screen. */
        public class TotpMain(public val component: TotpMainComponent) : Child

        /** TOTP recovery codes management. */
        public class TotpRecoveryCodes(public val component: TotpRecoveryCodesComponent) : Child

        /** Active session management. */
        public class Sessions(public val component: SessionListComponent) : Child, SessionListOwner by component

        /** Active session detail screen. */
        public class SessionDetail(public val component: SessionDetailComponent) : Child

        /** Account identifiers management. */
        public class Identifiers(
            public val component: IdentifierListComponent
        ) : Child, IdentifierListOwner by component

        /** Identifier detail screen. */
        public class IdentifierDetail(public val component: IdentifierDetailComponent) : Child

        /** User storage file list screen. */
        public class StorageFileList(public val component: StorageFileListComponent) : Child

        /** User storage file detail screen. */
        public class StorageFileDetail(public val component: StorageFileDetailComponent) : Child

        /** User storage file upload screen. */
        public class StorageFileUpload(public val component: StorageFileUploadScreenComponent) : Child
    }
}
