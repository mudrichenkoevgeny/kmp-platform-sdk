package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.edit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit.UserEditScreenState
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate

@InternalApi
class UserEditPreviewProvider : PreviewParameterProvider<UserEditScreenState> {
    private val testUser = createTestUserPrivate()

    override val values: Sequence<UserEditScreenState> = sequenceOf(
        UserEditScreenState.Loading,
        UserEditScreenState.Content(
            user = testUser,
            nicknameInput = "johndoe",
            displayNameInput = "John Doe"
        ),
        UserEditScreenState.Content(
            user = testUser,
            nicknameInput = "jo",
            displayNameInput = "John",
            nicknameError = UserError.NicknameTooShort(3)
        ),
        UserEditScreenState.Content(
            user = testUser,
            nicknameInput = "johndoe",
            displayNameInput = "John Doe",
            isSaving = true
        ),
        UserEditScreenState.Error(
            error = CommonError.Unknown()
        )
    )
}
