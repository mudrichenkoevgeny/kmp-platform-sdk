package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toInitialLoading
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toNextPageLoading
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSessionFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSessionSortBy
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Default [GlobalSessionListComponent] implementation: manages all sessions list globally.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Global session list parameters.
 * @param managementGetSessionsUseCase Use case to fetch paginated list of global sessions.
 * @param managementDeleteSessionUseCase Use case to administratively delete a session.
 * @param onNavigateToSessionDetail Callback invoked when a session is tapped.
 * @param onBack Pops this screen from the navigation stack.
 */
class GlobalSessionListComponentImpl(
    componentContext: ComponentContext,
    private val params: ManagementGlobalSessionListParams = ManagementGlobalSessionListParams(),
    private val managementGetSessionsUseCase: ManagementGetSessionsUseCase,
    private val managementDeleteSessionUseCase: ManagementDeleteSessionUseCase? = null,
    private val authStorage: AuthStorage? = null,
    private val userRepository: UserRepository? = null,
    private val onNavigateToSessionDetail: ((UserSessionSummary) -> Unit)? = null,
    private val onBack: () -> Unit
) : GlobalSessionListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<GlobalSessionListScreenState>(GlobalSessionListScreenState.Loading)
    override val state: Value<GlobalSessionListScreenState> = _state

    init {
        loadSessions()
    }

    override fun onRefresh() {
        loadSessions()
    }

    override fun onSessionClick(session: UserSessionSummary) {
        onNavigateToSessionDetail?.invoke(session)
    }

    override fun onRevokeClick(session: UserSessionSummary) {
        val deleteUseCase = managementDeleteSessionUseCase ?: return
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
        if (currentContent.actionLoading) {
            return
        }

        _state.value = currentContent.copy(actionLoading = true, actionError = null)

        scope.launch {
            deleteUseCase(
                userId = session.userId,
                sessionId = session.id.asHexDashString()
            )
                .onSuccess {
                    val updatedItems = currentContent.paging.items.filterNot { it.id == session.id }
                    _state.value = currentContent.copy(
                        paging = currentContent.paging.copy(items = updatedItems),
                        actionLoading = false,
                        actionError = null
                    )
                }
                .onError { error ->
                    _state.value = currentContent.copy(
                        actionLoading = false,
                        actionError = error
                    )
                }
        }
    }

    override fun onSessionRevoked(sessionId: UserSessionId) {
        val current = _state.value as? GlobalSessionListScreenState.Content ?: return
        val newItems = current.paging.items.filterNot { it.id == sessionId }
        _state.value = current.copy(
            paging = current.paging.copy(items = newItems)
        )
    }

    override fun onBackClick() {
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) {
            return
        }

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(
            pageNumber = paging.nextPageNumber,
            sortState = currentContent.sortState,
            filterStates = currentContent.filterStates
        )
    }

    override fun onToggleFilterPanel() {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = !currentContent.isFilterPanelExpanded)
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
        _state.value = currentContent.copy(sortState = sortState)
        loadSessions()
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
        val newFilterStates = currentContent.filterStates.toMutableMap()
        if (filterState == null) {
            newFilterStates.remove(filterId)
        } else {
            newFilterStates[filterId] = filterState
        }
        _state.value = currentContent.copy(filterStates = newFilterStates)
    }

    override fun onApplyFilters() {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = false)
        loadSessions()
    }

    private fun loadSessions() {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionLoading = false,
                actionError = null
            )
        } else {
            _state.value = GlobalSessionListScreenState.Loading
        }

        fetchPage(
            pageNumber = params.pageNumber,
            sortState = currentContent?.sortState,
            filterStates = currentContent?.filterStates
        )
    }

    private fun fetchPage(
        pageNumber: Int,
        sortState: ListingSortState?,
        filterStates: Map<String, ListingFilterState>?
    ) {
        val sortOrder = if (sortState?.isAscending == true) SortOrder.ASC else SortOrder.DESC
        val sortBy = sortState?.optionId?.let { UserSessionSortBy.fromValueOrNull(it) } ?: params.sortBy

        val sessionId = (filterStates?.get(UserSessionFilterValues.SESSION_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.sessionIds.ifEmpty { null }
        val userIds = (filterStates?.get(UserSessionFilterValues.USER_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.userIds.ifEmpty { null }
        val userRoles = (filterStates?.get(UserSessionFilterValues.USER_ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserRole.fromValueOrNull(it) } ?: params.userRoles.ifEmpty { null }
        val excludeUserRoles = (filterStates?.get(UserSessionFilterValues.EXCLUDE_USER_ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserRole.fromValueOrNull(it) } ?: params.excludeUserRoles.ifEmpty { null }
        val identifiers = (filterStates?.get(UserSessionFilterValues.IDENTIFIER) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.identifier?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val identifierIds = (filterStates?.get(UserSessionFilterValues.IDENTIFIER_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.identifierIds.ifEmpty { null }
        val identifierDisplayName = (filterStates?.get(UserSessionFilterValues.IDENTIFIER_DISPLAY_NAME) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.identifierDisplayName
        val userAuthProviders = (filterStates?.get(UserSessionFilterValues.IDENTIFIER_AUTH_PROVIDER) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAuthProvider.fromValueOrNull(it) } ?: params.identifierAuthProviders
        val excludeUserAuthProviders = (filterStates?.get(UserSessionFilterValues.EXCLUDE_IDENTIFIER_AUTH_PROVIDER) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAuthProvider.fromValueOrNull(it) } ?: params.excludeIdentifierAuthProviders
        val deviceIds = (filterStates?.get(UserSessionFilterValues.DEVICE_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.deviceId?.let { listOf(it) }
        val isDeviceIdSet = (filterStates?.get(UserSessionFilterValues.IS_DEVICE_ID_SET) as? BooleanListingFilterState)?.value ?: params.isDeviceIdSet
        val deviceNames = (filterStates?.get(UserSessionFilterValues.DEVICE_NAME) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.deviceName?.let { listOf(it) }
        val isDeviceNameSet = (filterStates?.get(UserSessionFilterValues.IS_DEVICE_NAME_SET) as? BooleanListingFilterState)?.value ?: params.isDeviceNameSet
        val clientTypes = (filterStates?.get(UserSessionFilterValues.CLIENT_TYPE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { ClientType.fromValueOrNull(it) } ?: params.clientTypes
        val excludeClientTypes = (filterStates?.get(UserSessionFilterValues.EXCLUDE_CLIENT_TYPE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { ClientType.fromValueOrNull(it) } ?: params.excludeClientTypes
        val isClientTypeSet = (filterStates?.get(UserSessionFilterValues.IS_CLIENT_TYPE_SET) as? BooleanListingFilterState)?.value ?: params.isClientTypeSet
        val userAgents = (filterStates?.get(UserSessionFilterValues.USER_AGENT) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.userAgent?.let { listOf(it) }
        val isUserAgentSet = (filterStates?.get(UserSessionFilterValues.IS_USER_AGENT_SET) as? BooleanListingFilterState)?.value ?: params.isUserAgentSet
        val ipAddresses = (filterStates?.get(UserSessionFilterValues.IP_ADDRESS) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.ipAddress?.let { listOf(it) }
        val isIpAddressSet = (filterStates?.get(UserSessionFilterValues.IS_IP_ADDRESS_SET) as? BooleanListingFilterState)?.value ?: params.isIpAddressSet
        val languages = (filterStates?.get(UserSessionFilterValues.LANGUAGE) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.language?.let { listOf(it) }
        val isLanguageSet = (filterStates?.get(UserSessionFilterValues.IS_LANGUAGE_SET) as? BooleanListingFilterState)?.value ?: params.isLanguageSet
        val appVersions = (filterStates?.get(UserSessionFilterValues.APP_VERSION) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.appVersion?.let { listOf(it) }
        val isAppVersionSet = (filterStates?.get(UserSessionFilterValues.IS_APP_VERSION_SET) as? BooleanListingFilterState)?.value ?: params.isAppVersionSet
        val operationSystemVersions = (filterStates?.get(UserSessionFilterValues.OPERATION_SYSTEM_VERSION) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.osVersion?.let { listOf(it) }
        val isOperationSystemVersionSet = (filterStates?.get(UserSessionFilterValues.IS_OPERATION_SYSTEM_VERSION_SET) as? BooleanListingFilterState)?.value ?: params.isOperationSystemVersionSet
        val expiresAtFrom = (filterStates?.get(UserSessionFilterValues.EXPIRES_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.expiresAtFrom
        val expiresAtTo = (filterStates?.get(UserSessionFilterValues.EXPIRES_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.expiresAtTo
        val lastAccessedAtFrom = (filterStates?.get(UserSessionFilterValues.LAST_ACCESSED_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastAccessedAtFrom
        val lastAccessedAtTo = (filterStates?.get(UserSessionFilterValues.LAST_ACCESSED_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastAccessedAtTo
        val lastReauthenticatedAtFrom = (filterStates?.get(UserSessionFilterValues.LAST_REAUTHENTICATED_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastReauthenticatedAtFrom
        val lastReauthenticatedAtTo = (filterStates?.get(UserSessionFilterValues.LAST_REAUTHENTICATED_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastReauthenticatedAtTo
        val createdAtFrom = (filterStates?.get(UserSessionFilterValues.CREATED_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.createdAtFrom
        val createdAtTo = (filterStates?.get(UserSessionFilterValues.CREATED_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.createdAtTo
        val updatedAtFrom = (filterStates?.get(UserSessionFilterValues.UPDATED_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.updatedAtFrom
        val updatedAtTo = (filterStates?.get(UserSessionFilterValues.UPDATED_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.updatedAtTo
        val isUpdatedAtSet = (filterStates?.get(UserSessionFilterValues.IS_UPDATED_AT_SET) as? BooleanListingFilterState)
            ?.value ?: params.isUpdatedAtSet
        val searchQuery = (filterStates?.get(UserSessionFilterValues.SEARCH_QUERY) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.searchQuery?.takeIf { it.isNotBlank() }

        scope.launch {
            managementGetSessionsUseCase(
                params = ManagementGlobalSessionListParams(
                    pageNumber = pageNumber,
                    sortBy = sortBy,
                    sortOrder = sortOrder,
                    sessionIds = sessionId ?: emptyList(),
                    userIds = userIds ?: emptyList(),
                    userRoles = userRoles ?: emptyList(),
                    excludeUserRoles = excludeUserRoles ?: emptyList(),
                    identifier = identifiers?.firstOrNull() ?: "",
                    identifierIds = identifierIds ?: emptyList(),
                    identifierDisplayName = identifierDisplayName ?: "",
                    identifierAuthProviders = userAuthProviders,
                    excludeIdentifierAuthProviders = excludeUserAuthProviders,
                    deviceId = deviceIds?.firstOrNull() ?: "",
                    isDeviceIdSet = isDeviceIdSet,
                    deviceName = deviceNames?.firstOrNull() ?: "",
                    isDeviceNameSet = isDeviceNameSet,
                    clientTypes = clientTypes,
                    excludeClientTypes = excludeClientTypes,
                    isClientTypeSet = isClientTypeSet,
                    userAgent = userAgents?.firstOrNull() ?: "",
                    isUserAgentSet = isUserAgentSet,
                    ipAddress = ipAddresses?.firstOrNull() ?: "",
                    isIpAddressSet = isIpAddressSet,
                    language = languages?.firstOrNull() ?: "",
                    isLanguageSet = isLanguageSet,
                    appVersion = appVersions?.firstOrNull() ?: "",
                    isAppVersionSet = isAppVersionSet,
                    osVersion = operationSystemVersions?.firstOrNull() ?: "",
                    isOperationSystemVersionSet = isOperationSystemVersionSet,
                    expiresAtFrom = expiresAtFrom ?: "",
                    expiresAtTo = expiresAtTo ?: "",
                    lastAccessedAtFrom = lastAccessedAtFrom ?: "",
                    lastAccessedAtTo = lastAccessedAtTo ?: "",
                    lastReauthenticatedAtFrom = lastReauthenticatedAtFrom ?: "",
                    lastReauthenticatedAtTo = lastReauthenticatedAtTo ?: "",
                    createdAtFrom = createdAtFrom ?: "",
                    createdAtTo = createdAtTo ?: "",
                    updatedAtFrom = updatedAtFrom ?: "",
                    updatedAtTo = updatedAtTo ?: "",
                    isUpdatedAtSet = isUpdatedAtSet,
                    searchQuery = searchQuery ?: ""
                )
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? GlobalSessionListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserSessionSummary>()).appendResult(pagedResult)
                    val activeSessionId = authStorage?.getSessionId()?.toUserSessionIdOrNull()
                    val currentUserId = userRepository?.currentUser?.firstOrNull()?.id
                    _state.value = currentContent?.copy(
                        paging = newPaging,
                        currentSessionId = activeSessionId,
                        currentUserId = currentUserId
                    ) ?: GlobalSessionListScreenState.Content(
                        paging = newPaging,
                        currentSessionId = activeSessionId,
                        currentUserId = currentUserId
                    )
                }
                .onError { error ->
                    val currentContent = _state.value as? GlobalSessionListScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = GlobalSessionListScreenState.Error(error)
                    }
                }
        }
    }
}
