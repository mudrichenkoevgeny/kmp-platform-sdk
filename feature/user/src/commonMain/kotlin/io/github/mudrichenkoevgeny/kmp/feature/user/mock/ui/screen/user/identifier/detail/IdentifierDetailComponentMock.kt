package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.identifier.detail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail.IdentifierDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail.IdentifierDetailScreenState
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierPrivate

/**
 * Mock implementation of [IdentifierDetailComponent] for Compose previews and testing.
 */
@InternalApi
public class IdentifierDetailComponentMock(
    initialState: IdentifierDetailScreenState = IdentifierDetailScreenState.Content(
        identifier = createTestUserIdentifierPrivate(),
        isCurrentIdentifier = false,
        canChangePassword = true,
        canDeletePassword = false
    )
) : IdentifierDetailComponent {
    public override val state: Value<IdentifierDetailScreenState> = MutableValue(initialState)

    public var deleteIdentifierCalls: Int = 0
    public var changePasswordCalls: Int = 0
    public var confirmChangePasswordCalls: Int = 0
    public var dismissChangePasswordCalls: Int = 0
    public var deletePasswordCalls: Int = 0
    public var retryCalls: Int = 0
    public var backCalls: Int = 0

    public var deleteIdentifierRequestedCalls: Int = 0
    public var dismissDeleteIdentifierDialogCalls: Int = 0

    public override fun onDeleteIdentifierRequested() {
        deleteIdentifierRequestedCalls++
    }

    public override fun onDismissDeleteIdentifierDialog() {
        dismissDeleteIdentifierDialogCalls++
    }

    public override fun onDeleteIdentifierClick() {
        deleteIdentifierCalls++
    }

    public override fun onChangePasswordClick() {
        changePasswordCalls++
    }

    public override fun onConfirmChangePasswordClick(
        oldPassword: String,
        newPassword: String
    ) {
        confirmChangePasswordCalls++
    }

    public override fun onDismissChangePasswordDialog() {
        dismissChangePasswordCalls++
    }

    public override fun onDeletePasswordClick() {
        deletePasswordCalls++
    }

    public override fun onRetry() {
        retryCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }

    public override fun onUserClick(): Unit = Unit
}
