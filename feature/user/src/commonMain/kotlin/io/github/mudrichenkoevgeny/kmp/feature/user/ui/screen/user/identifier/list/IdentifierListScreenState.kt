package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary

/**
 * Universal screen state hierarchy for identifier list screens (client and management).
 */
public sealed interface IdentifierListScreenState {

    /** Initial loading state. */
    public data object Loading : IdentifierListScreenState

    /**
     * Global error state.
     *
     * @property error The application error that occurred during initialization or fetch.
     */
    public data class Error(val error: AppError) : IdentifierListScreenState

    /**
     * Active content state displaying paginated user identifiers with optional filter/sort options.
     *
     * @property paging Current pagination state of user identifiers.
     * @property currentIdentifierId Active identifier ID if resolved.
     * @property currentUserId Current authenticated user ID if resolved.
     * @property isAddIdentifierSupported Whether adding a new identifier is allowed.
     * @property availableAuthProviders Allowed providers for adding identifiers.
     * @property addEmail Entered email in add identifier dialog.
     * @property isAddEmailValid Whether [addEmail] format is valid.
     * @property addPhone Entered phone number in add identifier dialog.
     * @property isAddPhoneValid Whether [addPhone] format is valid.
     * @property addCode Confirmation OTP code.
     * @property addPassword Entered password for email identifier binding.
     * @property isAddPasswordValid Whether [addPassword] is valid.
     * @property isAddPasswordVisible Whether [addPassword] text is plain text or masked.
     * @property addResendTimerSeconds Remaining seconds until OTP resend is allowed.
     * @property isFilterPanelExpanded Whether the filter options panel is currently open.
     * @property sortState Active sort option.
     * @property filterStates Map of active filter IDs to filter states.
     * @property actionLoading Whether an async action (such as adding an identifier) is in progress.
     * @property actionError Error resulting from an async action attempt.
     */
    public data class Content(
        val paging: PaginationState<UserIdentifierSummary> = PaginationState(),
        val currentIdentifierId: UserIdentifierId? = null,
        val currentUserId: UserId? = null,
        val isAddIdentifierSupported: Boolean = false,
        val availableAuthProviders: AvailableAuthProviders? = null,
        val addEmail: String = "",
        val isAddEmailValid: Boolean = false,
        val addPhone: String = "",
        val isAddPhoneValid: Boolean = false,
        val addCode: String = "",
        val addPassword: String = "",
        val isAddPasswordValid: Boolean = false,
        val isAddPasswordVisible: Boolean = false,
        val addResendTimerSeconds: Int = 0,
        val isFilterPanelExpanded: Boolean = false,
        val sortState: ListingSortState? = null,
        val filterStates: Map<String, ListingFilterState> = emptyMap(),
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : IdentifierListScreenState {

        /** Whether the confirmation code can be resent. */
        public val canResendCode: Boolean
            get() = addResendTimerSeconds <= 0 && !actionLoading

        /** Whether the add email identifier form can be submitted. */
        public val canSubmitAddEmail: Boolean
            get() = isAddEmailValid && isAddPasswordValid && addCode.isNotBlank() && !actionLoading

        /** Whether the add phone identifier form can be submitted. */
        public val canSubmitAddPhone: Boolean
            get() = isAddPhoneValid && addCode.isNotBlank() && !actionLoading
    }
}
