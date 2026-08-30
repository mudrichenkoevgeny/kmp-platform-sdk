package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.profile

import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ProfileScreenComponentImplTest {

    @Test
    fun currentUserNull_emitsUnauthorized_andLoginClickInvokesCallback() = runComponentTest {
        val repo = UserRepositoryMock()
        repo.emit(null)

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        var loginRequests = 0
        val component = ProfileScreenComponentImpl(
            componentContext = ctx,
            userRepository = repo,
            onLoginDialogRequest = { loginRequests++ }
        )
        advanceUntilIdle()
        assertIs<ProfileScreenState.Unauthorized>(component.state.value)
        component.onLoginClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, loginRequests)
        lifecycle.destroy()
    }

    @Test
    fun currentUserNonNull_emitsContent() = runComponentTest {
        val user = userDetailsMock()
        val repo = UserRepositoryMock()
        repo.emit(user)

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val component = ProfileScreenComponentImpl(
            componentContext = ctx,
            userRepository = repo,
            onLoginDialogRequest = {}
        )
        advanceUntilIdle()
        val content = assertIs<ProfileScreenState.Content>(component.state.value)
        assertEquals<UserDetails?>(user, content.user)
        lifecycle.destroy()
    }

    @Test
    fun currentUserFlowFailure_emitsError() = runComponentTest {
        val repo = UserRepositoryMock().apply {
            currentUserProvider = {
                flow { throw IllegalStateException("user stream failed") }
            }
        }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val component = ProfileScreenComponentImpl(
            componentContext = ctx,
            userRepository = repo,
            onLoginDialogRequest = {}
        )
        advanceUntilIdle()
        val err = assertIs<ProfileScreenState.Error>(component.state.value)
        assertIs<CommonError.Unknown>(err.appError)
        lifecycle.destroy()
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}