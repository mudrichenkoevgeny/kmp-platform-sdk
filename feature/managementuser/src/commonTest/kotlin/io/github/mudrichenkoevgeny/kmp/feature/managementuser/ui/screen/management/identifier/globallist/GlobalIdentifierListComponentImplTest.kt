package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier.ManagementIdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class GlobalIdentifierListComponentImplTest {

    @Test
    fun init_loadsIdentifiersSuccessfully() = runComponentTest {
        val identifier = createTestUserIdentifierSummary()
        val context = createTestContext(identifiers = listOf(identifier))

        advanceUntilIdle()
        val state = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertEquals(listOf(identifier), state.paging.items)
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val repository = ManagementIdentifierRepositoryMock()
        val error = CommonError.Unknown()
        repository.getIdentifiersResultProvider = { AppResult.Error(error) }
        val context = createTestContext(repository = repository)

        advanceUntilIdle()
        val state = assertIs<GlobalIdentifierListScreenState.Error>(context.component.state.value)
        assertEquals(error, state.error)
    }

    @Test
    fun onToggleFilterPanel_togglesIsFilterPanelExpanded() = runComponentTest {
        val context = createTestContext(identifiers = listOf(createTestUserIdentifierSummary()))
        advanceUntilIdle()

        var state = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertFalse(state.isFilterPanelExpanded)

        context.component.onToggleFilterPanel()
        state = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertTrue(state.isFilterPanelExpanded)
    }

    @Test
    fun onApplyFilters_appliesFiltersAndReloads() = runComponentTest {
        val repository = ManagementIdentifierRepositoryMock()
        val context = createTestContext(repository = repository, identifiers = listOf(createTestUserIdentifierSummary()))
        advanceUntilIdle()

        context.component.onFilterChanged(
            UserFilterValues.UserIdentifierFilterValues.IDENTIFIER,
            TextListingFilterState(value = "test@example.com")
        )
        context.component.onApplyFilters()
        advanceUntilIdle()

        assertEquals(listOf("test@example.com"), repository.lastIdentifiers)
    }

    @Test
    fun onSortChanged_appliesSortAndReloads() = runComponentTest {
        val repository = ManagementIdentifierRepositoryMock()
        val context = createTestContext(repository = repository, identifiers = listOf(createTestUserIdentifierSummary()))
        advanceUntilIdle()

        context.component.onSortChanged(ListingSortState(optionId = UserSortValues.UserIdentifierSortBy.CREATED_AT.value, isAscending = false))
        advanceUntilIdle()

        assertEquals(UserSortValues.UserIdentifierSortBy.CREATED_AT, repository.lastSortBy)
    }

    @Test
    fun onRefresh_reloadsIdentifiers() = runComponentTest {
        val context = createTestContext(identifiers = listOf(createTestUserIdentifierSummary()))
        advanceUntilIdle()

        context.component.onRefresh()
        advanceUntilIdle()

        assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    private fun createTestContext(
        identifiers: List<UserIdentifierSummary>? = null,
        repository: ManagementIdentifierRepositoryMock = ManagementIdentifierRepositoryMock()
    ): TestContext {
        if (identifiers != null) {
            repository.getIdentifiersResultProvider = { AppResult.Success(createTestPagedResult(identifiers)) }
        }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val useCase = ManagementGetIdentifiersUseCase(repository)
        val context = TestContext()

        context.component = GlobalIdentifierListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            managementGetIdentifiersUseCase = useCase,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext {
        lateinit var component: GlobalIdentifierListComponentImpl
        var onBackCalls = 0
    }
}