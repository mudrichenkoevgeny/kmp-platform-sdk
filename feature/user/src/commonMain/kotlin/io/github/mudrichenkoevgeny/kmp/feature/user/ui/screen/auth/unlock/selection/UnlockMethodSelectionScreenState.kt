package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/** UI state for selecting an account unlock channel. */
data class UnlockMethodSelectionScreenState(
    val lockoutType: AccountLockoutType? = null,
    val lockoutUntil: Long? = null,
    val knownIdentifiers: List<UserIdentifier> = emptyList(),
    val actionLoading: Boolean = false,
    val actionError: AppError? = null
) {
    /** True if email unlock is applicable (either user has email identifier or list is empty/unauthenticated). */
    val isEmailAvailable: Boolean
        get() = knownIdentifiers.isEmpty() || knownIdentifiers.any { it.userAuthProvider == UserAuthProvider.EMAIL }

    /** True if phone unlock is applicable (either user has phone identifier or list is empty/unauthenticated). */
    val isPhoneAvailable: Boolean
        get() = knownIdentifiers.isEmpty() || knownIdentifiers.any { it.userAuthProvider == UserAuthProvider.PHONE }

    /** True if Google unlock is applicable (either user has Google identifier or list is empty/unauthenticated). */
    val isGoogleAvailable: Boolean
        get() = knownIdentifiers.isEmpty() || knownIdentifiers.any { it.userAuthProvider == UserAuthProvider.GOOGLE }

    /** True if Apple unlock is applicable (either user has Apple identifier or list is empty/unauthenticated). */
    val isAppleAvailable: Boolean
        get() = knownIdentifiers.isEmpty() || knownIdentifiers.any { it.userAuthProvider == UserAuthProvider.APPLE }
}
