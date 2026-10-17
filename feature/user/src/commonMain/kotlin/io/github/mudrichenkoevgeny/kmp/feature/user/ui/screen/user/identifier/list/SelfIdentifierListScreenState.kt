package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary

/**
 * UI state for the user identifiers management.
 */
sealed interface SelfIdentifierListScreenState {

    /** Initial loading of identifiers. */
    data object Loading : SelfIdentifierListScreenState

    /**
     * Identifiers list with pagination and dialog states.
     *
     * @param paging cumulative state of the paginated list.
     * @param currentIdentifierId the identifier ID used to authorize current session.
     * @param availableAuthProviders allowed auth providers for adding identifiers.
     * @param isAddIdentifierSupported whether adding new identifiers is enabled.
     * @param addEmail target email input.
     * @param isAddEmailValid whether [addEmail] passes client-side validation.
     * @param addPhone target phone input.
     * @param isAddPhoneValid whether [addPhone] passes client-side validation.
     * @param addCode confirmation code input.
     * @param addPassword password for confirming new email identifier.
     * @param isAddPasswordValid whether [addPassword] passes basic checks.
     * @param isAddPasswordVisible toggles visibility of the password field.
     * @param addResendTimerSeconds seconds until resend is allowed.
     * @param actionLoading true when an action is in progress.
     * @param actionError error from the last action attempt.
     */
    data class Content(
        val paging: PaginationState<UserIdentifierSummary> = PaginationState(),
        val currentIdentifierId: UserIdentifierId? = null,
        val availableAuthProviders: AvailableAuthProviders? = null,
        val isAddIdentifierSupported: Boolean = false,
        val addEmail: String = "",
        val isAddEmailValid: Boolean = false,
        val addPhone: String = "",
        val isAddPhoneValid: Boolean = false,
        val addCode: String = "",
        val addPassword: String = "",
        val isAddPasswordValid: Boolean = false,
        val isAddPasswordVisible: Boolean = false,
        val addResendTimerSeconds: Int = 0,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : SelfIdentifierListScreenState {
        val canSubmitAddEmail: Boolean get() = isAddPasswordValid && addCode.length == FieldValidator.DEFAULT_OTP_LENGTH && !actionLoading
        val canSubmitAddPhone: Boolean get() = addCode.length == FieldValidator.DEFAULT_OTP_LENGTH && !actionLoading
        val canResendCode: Boolean get() = addResendTimerSeconds <= 0 && !actionLoading
    }

    /** Critical error during state initialization. */
    data class Error(val error: AppError) : SelfIdentifierListScreenState
}