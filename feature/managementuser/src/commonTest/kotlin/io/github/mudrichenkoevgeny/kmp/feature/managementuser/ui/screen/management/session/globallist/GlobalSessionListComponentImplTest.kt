package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.list.datasource.ManagementSessionListDataSource
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSessionFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSessionSortBy
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary
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
        val session = createTestUserSessionSummary()
        val context = createTestContext(sessions = listOf(session))

        advanceUntilIdle()
        val state = assertIs<SessionListScreenState.Content>(context.component.state.value)
        assertEquals(listOf(session), state.paging.items)
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val repository = ManagementSessionRepositoryMock()
        val error = CommonError.Unknown()
        repository.getSessionsResultProvider = { AppResult.Error(error) }
        val context = createTestContext(repository = repository)

        advanceUntilIdle()
        val state = assertIs<SessionListScreenState.Error>(context.component.state.value)
        assertEquals(error, state.error)
    }

    @Test
    fun onToggleFilterPanel_togglesIsFilterPanelExpanded() = runComponentTest {
        val context = createTestContext(sessions = listOf(createTestUserSessionSummary()))
        advanceUntilIdle()

        var state = assertIs<SessionListScreenState.Content>(context.component.state.value)
        assertFalse(state.isFilterPanelExpanded)

        context.component.onToggleFilterPanel()
        state = assertIs<SessionListScreenState.Content>(context.component.state.value)
        assertTrue(state.isFilterPanelExpanded)
    }

    @Test
    fun onApplyFilters_appliesFiltersAndReloads() = runComponentTest {
        val repository = ManagementSessionRepositoryMock()
        val context = createTestContext(repository = repository, sessions = listOf(createTestUserSessionSummary()))
        advanceUntilIdle()

        context.component.onFilterChanged(
            UserSessionFilterValues.IP_ADDRESS,
            TextListingFilterState(value = "127.0.0.1")
        )
        context.component.onApplyFilters()
        advanceUntilIdle()

        assertEquals("127.0.0.1", repository.lastGlobalParams?.ipAddress)
    }

    @Test
    fun onSortChanged_appliesSortAndReloads() = runComponentTest {
        val repository = ManagementSessionRepositoryMock()
        val context = createTestContext(repository = repository, sessions = listOf(createTestUserSessionSummary()))
        advanceUntilIdle()

        context.component.onSortChanged(
            ListingSortState(optionId = UserSessionSortBy.CREATED_AT.value, isAscending = false)
        )
        advanceUntilIdle()

        assertEquals(UserSessionSortBy.CREATED_AT, repository.lastGlobalParams?.sortBy)
    }

    @Test
    fun onRefresh_reloadsSessions() = runComponentTest {
        val context = createTestContext(sessions = listOf(createTestUserSessionSummary()))
        advanceUntilIdle()

        context.component.onRefresh()
        advanceUntilIdle()

        assertIs<SessionListScreenState.Content>(context.component.state.value)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    private fun createTestContext(
        sessions: List<UserSessionSummary>? = null,
        repository: ManagementSessionRepositoryMock = ManagementSessionRepositoryMock()
    ): TestContext {
        if (sessions != null) {
            repository.getSessionsResultProvider = { AppResult.Success(createTestPagedResult(sessions)) }
        }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val getUseCase = ManagementGetSessionsUseCase(repository)
        val deleteUseCase = ManagementDeleteSessionUseCase(repository)
        val context = TestContext()

        context.component = SessionListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            dataSource = ManagementSessionListDataSource(
                managementGetSessionsUseCase = getUseCase,
                managementDeleteSessionUseCase = deleteUseCase
            ),
            includeFilterPanel = true,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext {
        lateinit var component: SessionListComponentImpl
        var onBackCalls = 0
    }
}
