package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings.openAuthSettingsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings.OpenAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.GetUserIdentifiersUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class SelfIdentifierListComponentImplTest {

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
        val context = createTestContext(getUserIdentifiersUseCase = getUserIdentifiersUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<SelfIdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(identifiers, state.paging.items)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onIdentifierClick_invokesCallback() = runComponentTest {
        var selectedId: UserIdentifierId? = null
        val context = createTestContext(
            onIdentifierSelect = { selectedId = it }
        )
        try {
            advanceTimeBy(100.milliseconds)
            val id = UserIdentifierId.generate()
            context.component.onIdentifierClick(id)
            assertEquals(id, selectedId)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onAddIdentifierClick_opensProviderSelectionDialog() = runComponentTest {
        val settingsRepo = OpenAuthSettingsRepositoryMock().apply {
            emit(openAuthSettingsMock())
        }
        val getAvailableUserAuthProvidersUseCase = GetAvailableUserAuthProvidersUseCase(
            appType = AppType.CLIENT,
            openAuthSettingsRepository = settingsRepo
        )
        val context = createTestContext(
            getAvailableUserAuthProvidersUseCase = getAvailableUserAuthProvidersUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onAddIdentifierClick()
            advanceTimeBy(100.milliseconds)
            val state = assertIs<SelfIdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(AddIdentifierDialogState.ProviderSelection, state.addIdentifierDialogState)

            context.component.onAddIdentifierSelectProvider(UserAuthProvider.EMAIL)
            val updatedState = assertIs<SelfIdentifierListScreenState.Content>(context.component.state.value)
            assertIs<AddIdentifierDialogState.EmailFlow>(updatedState.addIdentifierDialogState)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onIdentifierDeleted_removesItemFromPaging() = runComponentTest {
        val item1 = userIdentifierMock()
        val item2 = userIdentifierMock()
        val getUserIdentifiersUseCase = GetUserIdentifiersUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(
                    PagedResult(
                        items = listOf(item1, item2),
                        totalCount = 2,
                        pageNumber = 1,
                        pageSize = 10,
                        totalPages = 1
                    )
                )
            }
        }
        val context = createTestContext(getUserIdentifiersUseCase = getUserIdentifiersUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onIdentifierDeleted(item1.id)
            val state = assertIs<SelfIdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(listOf(item2), state.paging.items)
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
        val context = createTestContext(getUserIdentifiersUseCase = getUserIdentifiersUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            val state1 = assertIs<SelfIdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(initialIdentifiers, state1.paging.items)

            context.component.onLoadNextPage()
            advanceTimeBy(100.milliseconds)

            val state2 = assertIs<SelfIdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(initialIdentifiers + nextIdentifiers, state2.paging.items)
            assertEquals(2, getUserIdentifiersUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        val context = createTestContext()
        try {
            context.component.onBackClick()
            assertEquals(1, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createTestContext(
        getUserIdentifiersUseCase: GetUserIdentifiersUseCaseMock = GetUserIdentifiersUseCaseMock(),
        getAvailableUserAuthProvidersUseCase: GetAvailableUserAuthProvidersUseCase = GetAvailableUserAuthProvidersUseCase(
            appType = AppType.CLIENT,
            openAuthSettingsRepository = OpenAuthSettingsRepositoryMock().apply { emit(openAuthSettingsMock()) }
        ),
        onIdentifierSelect: (UserIdentifierId) -> Unit = {}
    ): TestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = TestContext(lifecycle)

        context.component = SelfIdentifierListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            getUserIdentifiersUseCase = getUserIdentifiersUseCase,
            getAvailableUserAuthProvidersUseCase = getAvailableUserAuthProvidersUseCase,
            onIdentifierSelect = onIdentifierSelect,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: SelfIdentifierListComponentImpl
        var onBackCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }
}
