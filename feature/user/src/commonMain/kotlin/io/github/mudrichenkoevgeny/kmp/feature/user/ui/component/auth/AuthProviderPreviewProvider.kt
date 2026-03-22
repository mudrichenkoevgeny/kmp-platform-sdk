package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider

/**
 * [PreviewParameterProvider] that yields a fixed sequence of [UserAuthProvider] values for Compose previews.
 */
class AuthProviderPreviewProvider : PreviewParameterProvider<UserAuthProvider> {
    /** Sample providers used in previews (email, phone, Google, Apple). */
    override val values: Sequence<UserAuthProvider> = sequenceOf(
        UserAuthProvider.EMAIL,
        UserAuthProvider.PHONE,
        UserAuthProvider.GOOGLE,
        UserAuthProvider.APPLE
    )
}