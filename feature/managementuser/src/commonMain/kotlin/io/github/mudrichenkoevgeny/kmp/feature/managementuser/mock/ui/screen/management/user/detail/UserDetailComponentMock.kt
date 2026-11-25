package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.detail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailScreenState
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserAccountStatus

/**
 * Mock implementation of [UserDetailComponent] for testing.
 */
@InternalApi
public class UserDetailComponentMock(
    initialState: UserDetailScreenState = UserDetailScreenState.Loading
) : UserDetailComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<UserDetailScreenState> = _state

    public var updateCalls: Int = 0
    public var deleteCalls: Int = 0
    public var confirmDeleteCalls: Int = 0
    public var dismissDeleteDialogCalls: Int = 0
    public var disableTotpCalls: Int = 0
    public var sessionsCalls: Int = 0
    public var identifiersCalls: Int = 0
    public var filesCalls: Int = 0
    public var auditEventsCalls: Int = 0
    public var retryCalls: Int = 0
    public var backCalls: Int = 0
    public var lastNicknameChanged: String? = null
    public var lastDisplayNameChanged: String? = null
    public var lastAvatarFileSelected: SelectedFilePayload? = null
    public var lastBirthdateChanged: String? = null
    public var lastAuthorityLevelChanged: String? = null
    public var lastAccountStatusChanged: UserAccountStatus? = null
    public var lastLockoutTypeChanged: String? = null
    public var lastTemporaryLockoutUntilChanged: String? = null
    public var lastQuotaBytesChanged: String? = null
    public var lastMaxUploadSizeBytesChanged: String? = null
    public var lastMaxPendingUploadsChanged: String? = null

    public override fun onNicknameChanged(value: String) {
        lastNicknameChanged = value
    }

    public override fun onDisplayNameChanged(value: String) {
        lastDisplayNameChanged = value
    }

    public override fun onAvatarFileSelected(payload: SelectedFilePayload) {
        lastAvatarFileSelected = payload
    }

    public override fun onBirthdateChanged(value: String) {
        lastBirthdateChanged = value
    }

    public override fun onAuthorityLevelChanged(value: String) {
        lastAuthorityLevelChanged = value
    }

    public override fun onAccountStatusChanged(value: UserAccountStatus) {
        lastAccountStatusChanged = value
    }

    public override fun onLockoutTypeChanged(value: String) {
        lastLockoutTypeChanged = value
    }

    public override fun onTemporaryLockoutUntilChanged(value: String) {
        lastTemporaryLockoutUntilChanged = value
    }

    public override fun onQuotaBytesChanged(value: String) {
        lastQuotaBytesChanged = value
    }

    public override fun onMaxUploadSizeBytesChanged(value: String) {
        lastMaxUploadSizeBytesChanged = value
    }

    public override fun onMaxPendingUploadsChanged(value: String) {
        lastMaxPendingUploadsChanged = value
    }

    public override fun onUpdateClick() {
        updateCalls++
    }

    public override fun onDeleteClick() {
        deleteCalls++
    }

    public override fun onConfirmDeleteClick() {
        confirmDeleteCalls++
    }

    public override fun onDismissDeleteDialog() {
        dismissDeleteDialogCalls++
    }

    public override fun onDisableTotpClick() {
        disableTotpCalls++
    }

    public override fun onSessionsClick() {
        sessionsCalls++
    }

    public override fun onIdentifiersClick() {
        identifiersCalls++
    }

    public override fun onFilesClick() {
        filesCalls++
    }

    public override fun onAuditEventsClick() {
        auditEventsCalls++
    }

    public override fun onRetry() {
        retryCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }
}
