package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

/**
 * State for the add identifier dialog progression on the self identifiers screen.
 */
sealed interface AddIdentifierDialogState {
    /** Step 1: Select provider. */
    data object ProviderSelection : AddIdentifierDialogState

    /** Step 2: Email input or confirmation code. */
    data class EmailFlow(
        val email: String = "",
        val isEmailValid: Boolean = false,
        val code: String = "",
        val password: String = "",
        val isPasswordValid: Boolean = false,
        val isPasswordVisible: Boolean = false,
        val isConfirmationSent: Boolean = false,
        val resendTimerSeconds: Int = 0,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : AddIdentifierDialogState {
        val canSendCode: Boolean get() = isEmailValid && !actionLoading
        val canSubmit: Boolean get() = isPasswordValid && code.length == FieldValidator.DEFAULT_OTP_LENGTH && !actionLoading
        val canResendCode: Boolean get() = resendTimerSeconds <= 0 && !actionLoading
    }

    /** Step 2: Phone input or confirmation code. */
    data class PhoneFlow(
        val phoneNumber: String = "",
        val isPhoneNumberValid: Boolean = false,
        val code: String = "",
        val isConfirmationSent: Boolean = false,
        val resendTimerSeconds: Int = 0,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : AddIdentifierDialogState {
        val canSendCode: Boolean get() = isPhoneNumberValid && !actionLoading
        val canSubmit: Boolean get() = code.length == FieldValidator.DEFAULT_OTP_LENGTH && !actionLoading
        val canResendCode: Boolean get() = resendTimerSeconds <= 0 && !actionLoading
    }
}

/**
 * UI state for the user identifiers management.
 */
sealed interface SelfIdentifierListScreenState {

    /** Initial loading of identifiers. */
    data object Loading : SelfIdentifierListScreenState

    /**
     * Identifiers list with pagination and optional add identifier dialog state.
     *
     * @param paging cumulative state of the paginated list.
     * @param currentIdentifierId the identifier ID used to authorize current session.
     * @param availableAuthProviders allowed auth providers for adding identifiers.
     * @param isAddIdentifierSupported whether adding new identifiers is enabled.
     * @param addIdentifierDialogState state of the add identifier dialog if open.
     * @param actionLoading true when an action is in progress.
     * @param actionError error from the last action attempt.
     */
    data class Content(
        val paging: PaginationState<UserIdentifier>,
        val currentIdentifierId: UserIdentifierId? = null,
        val availableAuthProviders: AvailableAuthProviders? = null,
        val isAddIdentifierSupported: Boolean = false,
        val addIdentifierDialogState: AddIdentifierDialogState? = null,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : SelfIdentifierListScreenState

    /** Critical error during state initialization. */
    data class Error(val error: AppError) : SelfIdentifierListScreenState
}
