package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.security.ManagementUserSecurityApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Implements [ManagementUserSecurityRepository] by forwarding administrative overrides to [ManagementUserSecurityApi].
 *
 * @param managementUserSecurityApi Administrative HTTP endpoints for user accounts security operations.
 */
class ManagementUserSecurityRepositoryImpl(
    private val managementUserSecurityApi: ManagementUserSecurityApi
) : ManagementUserSecurityRepository {

    override suspend fun disableTotp(userId: UserId): AppResult<Unit> {
        return managementUserSecurityApi.disableTotp(userId)
    }
}