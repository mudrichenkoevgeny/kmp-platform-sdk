package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * [PreviewParameterProvider] supplying sample lists of [UserAuthProvider] for [AuthProviderGrid] previews.
 */
@InternalApi
internal class AuthGridPreviewProvider : PreviewParameterProvider<List<UserAuthProvider>> {
    /** Varying list sizes and provider mixes for layout previews. */
    override val values: Sequence<List<UserAuthProvider>> = sequenceOf(
        listOf(UserAuthProvider.GOOGLE, UserAuthProvider.APPLE),
        listOf(
            UserAuthProvider.EMAIL,
            UserAuthProvider.PHONE,
            UserAuthProvider.GOOGLE,
            UserAuthProvider.APPLE
        ),
        List(6) { if (it % 2 == 0) UserAuthProvider.GOOGLE else UserAuthProvider.APPLE }
    )
}
