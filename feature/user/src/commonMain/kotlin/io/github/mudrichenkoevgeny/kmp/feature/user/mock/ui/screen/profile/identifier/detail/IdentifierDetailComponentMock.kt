package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier.detail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailScreenState

@InternalApi
class IdentifierDetailComponentMock(
    initialState: IdentifierDetailScreenState = IdentifierDetailScreenState.Content(
        identifier = userIdentifierMock(),
        isCurrentIdentifier = false,
        canChangePassword = true,
        canDeletePassword = false
    )
) : IdentifierDetailComponent {
    override val state: Value<IdentifierDetailScreenState> = MutableValue(initialState)

    var deleteIdentifierCalls = 0
    var changePasswordCalls = 0
    var confirmChangePasswordCalls = 0
    var dismissChangePasswordCalls = 0
    var deletePasswordCalls = 0
    var retryCalls = 0
    var backCalls = 0

    var deleteIdentifierRequestedCalls = 0
    var dismissDeleteIdentifierDialogCalls = 0

    override fun onDeleteIdentifierRequested() { deleteIdentifierRequestedCalls++ }
    override fun onDismissDeleteIdentifierDialog() { dismissDeleteIdentifierDialogCalls++ }
    override fun onDeleteIdentifierClick() { deleteIdentifierCalls++ }
    override fun onChangePasswordClick() { changePasswordCalls++ }
    override fun onConfirmChangePasswordClick(oldPassword: String, newPassword: String) { confirmChangePasswordCalls++ }
    override fun onDismissChangePasswordDialog() { dismissChangePasswordCalls++ }
    override fun onDeletePasswordClick() { deletePasswordCalls++ }
    override fun onRetry() { retryCalls++ }
    override fun onBackClick() { backCalls++ }
    override fun onUserClick() {}
}
