package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.security.password

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.PasswordChangeRequest

/** Authenticated password change for the current user. */
interface SecurityPasswordApi {
    /**
     * Changes the password for the signed-in user.
     *
     * @param request Current and new password payload from the shared contract.
     * @return Domain user identifier after the change, or a mapped failure.
     */
    suspend fun changePassword(request: PasswordChangeRequest): AppResult<UserIdentifier>
}