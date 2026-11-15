package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.edit

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit.UserEditComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit.UserEditScreenState
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate

/**
 * Mock implementation of [UserEditComponent] for Compose previews and testing.
 */
@InternalApi
public class UserEditComponentMock(
    initialState: UserEditScreenState = UserEditScreenState.Content(
        user = createTestUserPrivate(),
        nicknameInput = "johndoe",
        displayNameInput = "John Doe"
    )
) : UserEditComponent {

    private val _state: MutableValue<UserEditScreenState> = MutableValue(initialState)
    public override val state: Value<UserEditScreenState> = _state

    public var lastNicknameInput: String? = null
    public var lastDisplayNameInput: String? = null
    public var lastAvatarPayload: SelectedFilePayload? = null
    public var lastBirthdateInput: String? = null
    public var saveCalls: Int = 0
    public var backCalls: Int = 0
    public var retryCalls: Int = 0

    public override fun onNicknameChanged(value: String) {
        lastNicknameInput = value
        val current = _state.value as? UserEditScreenState.Content ?: return
        _state.value = current.copy(nicknameInput = value, nicknameError = null)
    }

    public override fun onDisplayNameChanged(value: String) {
        lastDisplayNameInput = value
        val current = _state.value as? UserEditScreenState.Content ?: return
        _state.value = current.copy(displayNameInput = value, displayNameError = null)
    }

    public override fun onAvatarFileSelected(payload: SelectedFilePayload) {
        lastAvatarPayload = payload
        val current = _state.value as? UserEditScreenState.Content ?: return
        _state.value = current.copy(selectedAvatarPayload = payload)
    }

    public override fun onBirthdateChanged(value: String) {
        lastBirthdateInput = value
        val current = _state.value as? UserEditScreenState.Content ?: return
        _state.value = current.copy(birthdateInput = value)
    }

    public override fun onSaveClick() {
        saveCalls++
    }

    public override fun onRetry() {
        retryCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }
}
