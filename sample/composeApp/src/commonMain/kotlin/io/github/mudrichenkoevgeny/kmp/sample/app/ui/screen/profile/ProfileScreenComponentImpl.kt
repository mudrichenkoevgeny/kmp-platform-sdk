package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.asValue
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ProfileScreenComponentImpl(
    componentContext: ComponentContext,
    private val userRepository: UserRepository,
    private val onLoginDialogRequest: () -> Unit
) : ProfileScreenComponent, ComponentContext by componentContext {

    override val state: Value<ProfileScreenState> = userRepository.currentUser
        .map { user ->
            if (user == null) {
                ProfileScreenState.Unauthorized
            } else {
                ProfileScreenState.Content(user = user)
            }
        }
        .catch { emit(ProfileScreenState.Error(CommonError.Unknown())) }
        .asValue(
            initialValue = ProfileScreenState.Loading,
            lifecycle = lifecycle
        )

    override fun onLoginClick() {
        onLoginDialogRequest()
    }
}