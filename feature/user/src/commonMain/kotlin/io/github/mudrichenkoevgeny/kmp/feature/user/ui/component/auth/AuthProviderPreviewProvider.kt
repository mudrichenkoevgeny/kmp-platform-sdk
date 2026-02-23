package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider

class AuthProviderPreviewProvider : PreviewParameterProvider<UserAuthProvider> {
    override val values: Sequence<UserAuthProvider> = sequenceOf(
        UserAuthProvider.EMAIL,
        UserAuthProvider.PHONE,
        UserAuthProvider.GOOGLE,
        UserAuthProvider.APPLE
    )
}