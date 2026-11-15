package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.create

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserScreenState

/**
 * Mock implementation of [CreateUserComponent] for testing.
 */
@InternalApi
public class CreateUserComponentMock(
    initialState: CreateUserScreenState = CreateUserScreenState()
) : CreateUserComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<CreateUserScreenState> = _state

    public var createCalls: Int = 0
    public var backCalls: Int = 0
    public var lastEmailChanged: String? = null
    public var lastPasswordChanged: String? = null
    public var lastNicknameChanged: String? = null
    public var lastDisplayNameChanged: String? = null
    public var lastAvatarFileSelected: SelectedFilePayload? = null
    public var lastBirthdateChanged: String? = null
    public var lastRoleChanged: String? = null
    public var lastStatusChanged: String? = null
    public var lastAuthorityLevelChanged: String? = null
    public var lastQuotaBytesChanged: String? = null
    public var lastMaxUploadSizeBytesChanged: String? = null
    public var lastMaxPendingUploadsChanged: String? = null

    /** Updates the current screen state. */
    public fun updateState(state: CreateUserScreenState) {
        _state.value = state
    }

    public override fun onEmailChanged(value: String) {
        lastEmailChanged = value
    }

    public override fun onPasswordChanged(value: String) {
        lastPasswordChanged = value
    }

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

    public override fun onRoleChanged(value: String) {
        lastRoleChanged = value
    }

    public override fun onStatusChanged(value: String) {
        lastStatusChanged = value
    }

    public override fun onAuthorityLevelChanged(value: String) {
        lastAuthorityLevelChanged = value
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

    public override fun onCreateClick() {
        createCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }
}
