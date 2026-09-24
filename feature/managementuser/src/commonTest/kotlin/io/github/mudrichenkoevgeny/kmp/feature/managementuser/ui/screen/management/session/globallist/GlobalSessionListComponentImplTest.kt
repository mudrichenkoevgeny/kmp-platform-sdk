package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class GlobalSessionListComponentImplTest {

    @Test
    fun init_loadsSessionsSuccessfully() = runComponentTest {
        val session = userSessionMock()
        val context = createTestContext(sessions = listOf(session))

        advanceUntilIdle()
        val state = assertIs<GlobalSessionListScreenState.Content>(context.component.state.value)
        assertEquals(listOf(session), state.paging.items)
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val repository = ManagementSessionRepositoryMock()
        val error = CommonError.Unknown()
        repository.getSessionsResultProvider = { AppResult.Error(error) }
        val context = createTestContext(repository = repository)

        advanceUntilIdle()
        val state = assertIs<GlobalSessionListScreenState.Error>(context.component.state.value)
        assertEquals(error, state.error)
    }

    @Test
    fun onRefresh_reloadsSessions() = runComponentTest {
        val context = createTestContext()
        advanceUntilIdle()

        context.component.onRefresh()

        advanceUntilIdle()
        assertIs<GlobalSessionListScreenState.Content>(context.component.state.value)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    @Test
    fun onToggleFilterPanel_togglesExpandedState() = runComponentTest {
        val context = createTestContext(sessions = emptyList())
        advanceUntilIdle()

        val initialState = assertIs<GlobalSessionListScreenState.Content>(context.component.state.value)
        assertFalse(initialState.isFilterPanelExpanded)

        context.component.onToggleFilterPanel()
        val expandedState = assertIs<GlobalSessionListScreenState.Content>(context.component.state.value)
        assertTrue(expandedState.isFilterPanelExpanded)

        context.component.onToggleFilterPanel()
        val collapsedState = assertIs<GlobalSessionListScreenState.Content>(context.component.state.value)
        assertFalse(collapsedState.isFilterPanelExpanded)
    }

    @Test
    fun onSortChanged_updatesSortState() = runComponentTest {
        val context = createTestContext(sessions = emptyList())
        advanceUntilIdle()

        val newSortState = ListingSortState(optionId = "created_at", isAscending = true)
        context.component.onSortChanged(newSortState)

        val state = assertIs<GlobalSessionListScreenState.Content>(context.component.state.value)
        assertEquals(newSortState, state.sortState)
    }

    @Test
    fun onFilterChanged_updatesAndRemovesFilters() = runComponentTest {
        val context = createTestContext(sessions = emptyList())
        advanceUntilIdle()

        val filterId = UserFilterValues.UserSessionFilterValues.IP_ADDRESS
        val filterState = TextListingFilterState(value = "127.0.0.1")

        context.component.onFilterChanged(filterId = filterId, filterState = filterState)
        var state = assertIs<GlobalSessionListScreenState.Content>(context.component.state.value)
        assertEquals(filterState, state.filterStates[filterId])

        context.component.onFilterChanged(filterId = filterId, filterState = null)
        state = assertIs<GlobalSessionListScreenState.Content>(context.component.state.value)
        assertFalse(state.filterStates.containsKey(filterId))
    }

    @Test
    fun onApplyFilters_closesPanelAndReloadsSessions() = runComponentTest {
        val context = createTestContext(sessions = emptyList())
        advanceUntilIdle()

        context.component.onToggleFilterPanel()
        assertTrue(assertIs<GlobalSessionListScreenState.Content>(context.component.state.value).isFilterPanelExpanded)

        context.component.onApplyFilters()
        advanceUntilIdle()

        val state = assertIs<GlobalSessionListScreenState.Content>(context.component.state.value)
        assertFalse(state.isFilterPanelExpanded)
    }

    @Test
    fun onDeleteSessionClick_success_reloadsSessions() = runComponentTest {
        val session = userSessionMock()
        val repository = ManagementSessionRepositoryMock()
        val context = createTestContext(sessions = listOf(session), repository = repository)
        advanceUntilIdle()

        val userId = UserId.generate()
        context.component.onDeleteSessionClick(userId = userId, sessionId = session.id.asHexDashString())

        advanceUntilIdle()
        val state = assertIs<GlobalSessionListScreenState.Content>(context.component.state.value)
        assertFalse(state.actionLoading)
    }

    private fun createTestContext(
        sessions: List<UserSession>? = null,
        repository: ManagementSessionRepositoryMock = ManagementSessionRepositoryMock()
    ): TestContext {
        if (sessions != null) {
            repository.getSessionsResultProvider = { AppResult.Success(pagedResultMock(sessions)) }
        }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val useCase = ManagementGetSessionsUseCase(repository)
        val deleteSessionUseCase = ManagementDeleteSessionUseCase(repository)
        val context = TestContext()

        context.component = GlobalSessionListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            managementGetSessionsUseCase = useCase,
            managementDeleteSessionUseCase = deleteSessionUseCase,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext {
        lateinit var component: GlobalSessionListComponentImpl
        var onBackCalls = 0
    }
}