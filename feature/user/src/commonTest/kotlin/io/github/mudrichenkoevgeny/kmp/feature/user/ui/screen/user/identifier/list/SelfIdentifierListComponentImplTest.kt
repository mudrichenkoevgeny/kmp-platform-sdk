package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings.openAuthSettingsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierSummaryMock
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
        val identifiers = listOf(userIdentifierSummaryMock())
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
    fun onIdentifierClick_invokesOnIdentifierSelect() = runComponentTest {
        var selectedId: UserIdentifierId? = null
        val context = createTestContext(onIdentifierSelect = { selectedId = it })
        try {
            val id = UserIdentifierId.generate()
            context.component.onIdentifierClick(id)
            assertEquals(id, selectedId)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onAddIdentifierClick_showsProviderSelectionDialog() = runComponentTest {
        val authSettingsRepo = OpenAuthSettingsRepositoryMock().apply {
            emit(openAuthSettingsMock())
        }
        val getAvailableUserAuthProvidersUseCase = GetAvailableUserAuthProvidersUseCase(
            appType = AppType.CLIENT,
            openAuthSettingsRepository = authSettingsRepo
        )
        val context = createTestContext(
            getAvailableUserAuthProvidersUseCase = getAvailableUserAuthProvidersUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onAddIdentifierClick()
            advanceTimeBy(100.milliseconds)

            assertIs<SelfIdentifierListComponent.AddIdentifierDialogChild.ProviderSelection>(context.component.addIdentifierDialogSlot.value.child?.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onAddIdentifierSelectProvider_email_showsEmailFlow() = runComponentTest {
        val context = createTestContext()
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onAddIdentifierClick()
            advanceTimeBy(100.milliseconds)

            context.component.onAddIdentifierSelectProvider(UserAuthProvider.EMAIL)
            assertIs<SelfIdentifierListComponent.AddIdentifierDialogChild.EmailInput>(context.component.addIdentifierDialogSlot.value.child?.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onIdentifierDeleted_removesItemFromPaging() = runComponentTest {
        val item1 = userIdentifierSummaryMock()
        val item2 = userIdentifierSummaryMock()
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
        val initialIdentifiers = listOf(userIdentifierSummaryMock())
        val nextIdentifiers = listOf(userIdentifierSummaryMock())
        val getUserIdentifiersUseCase = GetUserIdentifiersUseCaseMock().apply {
            resultProvider = { page, _ ->
                val items = if (page == ListingConstants.INITIAL_PAGE_NUMBER) initialIdentifiers else nextIdentifiers
                AppResult.Success(
                    PagedResult(
                        items = items,
                        totalCount = 2,
                        pageNumber = page ?: 1,
                        pageSize = 1,
                        totalPages = 2
                    )
                )
            }
        }
        val context = createTestContext(getUserIdentifiersUseCase = getUserIdentifiersUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onLoadNextPage()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<SelfIdentifierListScreenState.Content>(context.component.state.value)
            assertEquals(initialIdentifiers + nextIdentifiers, state.paging.items)
        } finally {
            context.destroy()
        }
    }

    private fun createTestContext(
        appType: AppType = AppType.CLIENT,
        getUserIdentifiersUseCase: GetUserIdentifiersUseCaseMock = GetUserIdentifiersUseCaseMock(),
        getAvailableUserAuthProvidersUseCase: GetAvailableUserAuthProvidersUseCase? = null,
        onIdentifierSelect: ((UserIdentifierId) -> Unit)? = null,
        onBack: () -> Unit = {}
    ): TestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = TestContext(lifecycle)

        context.component = SelfIdentifierListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            appType = appType,
            getUserIdentifiersUseCase = getUserIdentifiersUseCase,
            getAvailableUserAuthProvidersUseCase = getAvailableUserAuthProvidersUseCase,
            onIdentifierSelect = onIdentifierSelect,
            onBack = onBack
        )

        return context
    }

    private class TestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: SelfIdentifierListComponentImpl

        fun destroy() = lifecycle.destroy()
    }
}