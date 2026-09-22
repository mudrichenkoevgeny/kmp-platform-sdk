package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes

/** UI state for viewing and regenerating TOTP recovery codes. */
sealed interface TotpRecoveryCodesScreenState {

    /** Loading recovery codes from network. */
    data object Loading : TotpRecoveryCodesScreenState

    /**
     * Active recovery codes state.
     *
     * @param recoveryCodes active backup codes list.
     * @param showRegenerateConfirmation true when user tapped regenerate and needs dialog confirmation.
     * @param actionLoading true when regenerating codes.
     * @param actionError error from the last regenerate attempt.
     */
    data class Content(
        val recoveryCodes: TotpRecoveryCodes,
        val showRegenerateConfirmation: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : TotpRecoveryCodesScreenState

    /** Error during initial loading of recovery codes. */
    data class Error(val error: AppError) : TotpRecoveryCodesScreenState
}
