package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.model.user.mockCurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.test.runSampleComponentTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ProfileScreenComponentImplTest {

    @Test
    fun currentUserNull_emitsUnauthorized_andLoginClickInvokesCallback() = runSampleComponentTest {
        val userFlow = MutableStateFlow<CurrentUser?>(null)
        val repo = FakeUserRepository(userFlow)
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
    fun currentUserNonNull_emitsContent() = runSampleComponentTest {
        val user = mockCurrentUser()
        val userFlow = MutableStateFlow<CurrentUser?>(user)
        val repo = FakeUserRepository(userFlow)
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
        assertEquals(user, content.user)
        lifecycle.destroy()
    }

    @Test
    fun currentUserFlowFailure_emitsError() = runSampleComponentTest {
        val repo = FakeUserRepository(
            currentUser = flow { throw IllegalStateException("user stream failed") }
        )
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

private class FakeUserRepository(
    override val currentUser: Flow<CurrentUser?>
) : UserRepository
