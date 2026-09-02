package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.AddUserIdentifierEmailUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.AddUserIdentifierPhoneUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.DeleteUserIdentifierUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.EmailChangePasswordUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.GetUserIdentifiersUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.SendAddEmailIdentifierConfirmationUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.SendAddPhoneIdentifierConfirmationUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class IdentifierListComponentImplTest {

    @Test
    fun init_loadsIdentifiersSuccessfully() = runComponentTest {
        val identifiers = listOf(userIdentifierMock())
        val getUserIdentifiersUseCase = GetUserIdentifiersUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(
                    PagedResult(
                        items = identifiers,
                        totalCount = identifiers.size.toLong(),
                        pageNumber = 1,
                        pageSize = 10,
                        totalPages = 1
                    )
                )
            }
        }
        val context = createIdentifierListComponentTestContext(getUserIdentifiersUseCase = getUserIdentifiersUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<IdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(identifiers, state.paging.items)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onDeleteIdentifierClick_success_reloadsIdentifiers() = runComponentTest {
        val identifiers = listOf(userIdentifierMock())
        val identifierId = identifiers.first().id
        val getUserIdentifiersUseCase = GetUserIdentifiersUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(
                    PagedResult(
                        items = identifiers,
                        totalCount = identifiers.size.toLong(),
                        pageNumber = 1,
                        pageSize = 10,
                        totalPages = 1
                    )
                )
            }
        }
        val deleteUserIdentifierUseCase = DeleteUserIdentifierUseCaseMock()
        val context = createIdentifierListComponentTestContext(
            getUserIdentifiersUseCase = getUserIdentifiersUseCase,
            deleteUserIdentifierUseCase = deleteUserIdentifierUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            val initialLoadCalls = getUserIdentifiersUseCase.executeCalls

            context.component.onDeleteIdentifierClick(identifierId)
            advanceTimeBy(100.milliseconds)

            assertEquals(1, deleteUserIdentifierUseCase.executeCalls)
            assertEquals(initialLoadCalls + 1, getUserIdentifiersUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onAddEmailClick_success_movesToEnteringCode() = runComponentTest {
        val email = "test@example.com"
        val sendAddEmailIdentifierConfirmationUseCase = SendAddEmailIdentifierConfirmationUseCaseMock().apply {
            resultProvider = { AppResult.Success(OtpConfirmation(60, 6, 300)) }
        }
        val context = createIdentifierListComponentTestContext(
            sendAddEmailIdentifierConfirmationUseCase = sendAddEmailIdentifierConfirmationUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onAddEmailClick(email)
            advanceTimeBy(100.milliseconds)

            val state = assertIs<IdentifierListScreenState.Content>(context.component.state.value)
            val addState = assertIs<IdentifierListScreenState.AddIdentifierState.EnteringCode>(state.addEmailState)
            assertEquals(email, addState.value)
            assertEquals(1, sendAddEmailIdentifierConfirmationUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmAddEmailClick_success_reloadsIdentifiers() = runComponentTest {
        val email = "test@example.com"
        val code = "123456"
        val password = "password"
        val sendAddEmailIdentifierConfirmationUseCase = SendAddEmailIdentifierConfirmationUseCaseMock()
        val addUserIdentifierEmailUseCase = AddUserIdentifierEmailUseCaseMock()
        val context = createIdentifierListComponentTestContext(
            sendAddEmailIdentifierConfirmationUseCase = sendAddEmailIdentifierConfirmationUseCase,
            addUserIdentifierEmailUseCase = addUserIdentifierEmailUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onAddEmailClick(email)
            advanceTimeBy(100.milliseconds)
            context.component.onEmailCodeChanged(code)
            context.component.onConfirmAddEmailClick(password)
            advanceTimeBy(100.milliseconds)

            assertEquals(1, addUserIdentifierEmailUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onAddPhoneClick_success_movesToEnteringCode() = runComponentTest {
        val phone = "+1234567890"
        val sendAddPhoneIdentifierConfirmationUseCase = SendAddPhoneIdentifierConfirmationUseCaseMock().apply {
            resultProvider = { AppResult.Success(OtpConfirmation(60, 6, 300)) }
        }
        val context = createIdentifierListComponentTestContext(
            sendAddPhoneIdentifierConfirmationUseCase = sendAddPhoneIdentifierConfirmationUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onAddPhoneClick(phone)
            advanceTimeBy(100.milliseconds)

            val state = assertIs<IdentifierListScreenState.Content>(context.component.state.value)
            val addState = assertIs<IdentifierListScreenState.AddIdentifierState.EnteringCode>(state.addPhoneState)
            assertEquals(phone, addState.value)
            assertEquals(1, sendAddPhoneIdentifierConfirmationUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoadNextPage_success_appendsIdentifiers() = runComponentTest {
        val initialIdentifiers = listOf(userIdentifierMock())
        val nextIdentifiers = listOf(userIdentifierMock())
        val getUserIdentifiersUseCase = GetUserIdentifiersUseCaseMock().apply {
            resultProvider = { page, _ ->
                val items = if (page == ListingConstants.INITIAL_PAGE_NUMBER) initialIdentifiers else nextIdentifiers
                AppResult.Success(
                    PagedResult(
                        items = items,
                        totalCount = 2,
                        pageNumber = page,
                        pageSize = 1,
                        totalPages = 2
                    )
                )
            }
        }
        val context = createIdentifierListComponentTestContext(getUserIdentifiersUseCase = getUserIdentifiersUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            val state1 = assertIs<IdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(initialIdentifiers, state1.paging.items)

            context.component.onLoadNextPage()
            advanceTimeBy(100.milliseconds)

            val state2 = assertIs<IdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(initialIdentifiers + nextIdentifiers, state2.paging.items)
            assertEquals(2, getUserIdentifiersUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onChangePasswordClick_showsDialog_andConfirmExecutesUseCase() = runComponentTest {
        val email = "test@example.com"
        val oldPass = "old123"
        val newPass = "new123"
        val emailChangePasswordUseCase = EmailChangePasswordUseCaseMock()
        val context = createIdentifierListComponentTestContext(
            emailChangePasswordUseCase = emailChangePasswordUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onChangePasswordClick(email)

            val state1 = assertIs<IdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(email, state1.changePasswordEmail)

            context.component.onConfirmChangePasswordClick(oldPass, newPass)
            advanceTimeBy(100.milliseconds)

            assertEquals(1, emailChangePasswordUseCase.executeCalls)
            val state2 = assertIs<IdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(null, state2.changePasswordEmail)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        val context = createIdentifierListComponentTestContext()
        try {
            context.component.onBackClick()
            assertEquals(1, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createIdentifierListComponentTestContext(
        getUserIdentifiersUseCase: GetUserIdentifiersUseCaseMock = GetUserIdentifiersUseCaseMock(),
        deleteUserIdentifierUseCase: DeleteUserIdentifierUseCaseMock = DeleteUserIdentifierUseCaseMock(),
        sendAddEmailIdentifierConfirmationUseCase: SendAddEmailIdentifierConfirmationUseCaseMock = SendAddEmailIdentifierConfirmationUseCaseMock(),
        addUserIdentifierEmailUseCase: AddUserIdentifierEmailUseCaseMock = AddUserIdentifierEmailUseCaseMock(),
        sendAddPhoneIdentifierConfirmationUseCase: SendAddPhoneIdentifierConfirmationUseCaseMock = SendAddPhoneIdentifierConfirmationUseCaseMock(),
        addUserIdentifierPhoneUseCase: AddUserIdentifierPhoneUseCaseMock = AddUserIdentifierPhoneUseCaseMock(),
        emailChangePasswordUseCase: EmailChangePasswordUseCaseMock = EmailChangePasswordUseCaseMock()
    ): IdentifierListComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = IdentifierListComponentTestContext(lifecycle)

        context.component = IdentifierListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            getUserIdentifiersUseCase = getUserIdentifiersUseCase,
            deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
            sendAddEmailIdentifierConfirmationUseCase = sendAddEmailIdentifierConfirmationUseCase,
            addUserIdentifierEmailUseCase = addUserIdentifierEmailUseCase,
            sendAddPhoneIdentifierConfirmationUseCase = sendAddPhoneIdentifierConfirmationUseCase,
            addUserIdentifierPhoneUseCase = addUserIdentifierPhoneUseCase,
            emailChangePasswordUseCase = emailChangePasswordUseCase,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class IdentifierListComponentTestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: IdentifierListComponentImpl
        var onBackCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }
}
