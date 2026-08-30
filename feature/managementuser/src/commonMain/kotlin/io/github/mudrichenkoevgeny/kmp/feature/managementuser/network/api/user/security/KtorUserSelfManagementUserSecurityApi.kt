package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.security.UserSecurityApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totpsetup.TotpSetupPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.user.security.SelfManagementUserSecurityRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [UserSecurityApi] backed by [HttpClient]. */
class KtorUserSelfManagementUserSecurityApi(
    private val client: HttpClient
) : UserSecurityApi {

    override suspend fun setupTotp(): AppResult<TotpSetupPayload> = client.callResult {
        post(SelfManagementUserSecurityRoutes.SETUP_TOTP)
    }

    override suspend fun enableTotp(
        request: VerifyTotpPayload
    ): AppResult<TotpRecoveryCodesPayload> = client.callResult {
        post(SelfManagementUserSecurityRoutes.ENABLE_TOTP) {
            setBody(request)
        }
    }

    override suspend fun disableTotp(): AppResult<Unit> = client.callResult {
        delete(SelfManagementUserSecurityRoutes.DISABLE_TOTP)
    }

    override suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodesPayload> = client.callResult {
        get(SelfManagementUserSecurityRoutes.GET_RECOVERY_CODES)
    }

    override suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodesPayload> = client.callResult {
        post(SelfManagementUserSecurityRoutes.REGENERATE_RECOVERY_CODES)
    }
}