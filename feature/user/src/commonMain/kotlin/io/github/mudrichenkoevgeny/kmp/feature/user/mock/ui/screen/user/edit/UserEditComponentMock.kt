package io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.edit

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.SelectedFilePayload
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit.UserEditComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit.UserEditScreenState
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate

@InternalApi
class UserEditComponentMock(
    initialState: UserEditScreenState = UserEditScreenState.Content(
        user = createTestUserPrivate(),
        nicknameInput = "johndoe",
        displayNameInput = "John Doe"
    )
) : UserEditComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<UserEditScreenState> = _state

    var lastNicknameInput: String? = null
    var lastDisplayNameInput: String? = null
    var lastAvatarPayload: SelectedFilePayload? = null
    var lastBirthdateInput: String? = null
    var saveCalls = 0
    var backCalls = 0
    var retryCalls = 0

    override fun onNicknameChanged(value: String) {
        lastNicknameInput = value
        val current = _state.value as? UserEditScreenState.Content ?: return
        _state.value = current.copy(nicknameInput = value, nicknameError = null)
    }

    override fun onDisplayNameChanged(value: String) {
        lastDisplayNameInput = value
        val current = _state.value as? UserEditScreenState.Content ?: return
        _state.value = current.copy(displayNameInput = value, displayNameError = null)
    }

    override fun onAvatarFileSelected(payload: SelectedFilePayload) {
        lastAvatarPayload = payload
        val current = _state.value as? UserEditScreenState.Content ?: return
        _state.value = current.copy(selectedAvatarPayload = payload)
    }

    override fun onBirthdateChanged(value: String) {
        lastBirthdateInput = value
        val current = _state.value as? UserEditScreenState.Content ?: return
        _state.value = current.copy(birthdateInput = value)
    }

    override fun onSaveClick() {
        saveCalls++
    }

    override fun onRetry() {
        retryCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
