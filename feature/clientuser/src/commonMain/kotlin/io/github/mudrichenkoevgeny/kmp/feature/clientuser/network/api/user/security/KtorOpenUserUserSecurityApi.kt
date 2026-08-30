package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.security.UserSecurityApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totpsetup.TotpSetupPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.user.security.OpenUserSecurityRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class KtorOpenUserUserSecurityApi(
    private val client: HttpClient
) : UserSecurityApi {

    override suspend fun setupTotp(): AppResult<TotpSetupPayload> = client.callResult {
        post(OpenUserSecurityRoutes.SETUP_TOTP)
    }

    override suspend fun enableTotp(
        request: VerifyTotpPayload
    ): AppResult<TotpRecoveryCodesPayload> = client.callResult {
        post(OpenUserSecurityRoutes.ENABLE_TOTP) {
            setBody(request)
        }
    }

    override suspend fun disableTotp(): AppResult<Unit> = client.callResult {
        delete(OpenUserSecurityRoutes.DISABLE_TOTP)
    }

    override suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodesPayload> =
        client.callResult {
            get(OpenUserSecurityRoutes.GET_RECOVERY_CODES)
        }

    override suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodesPayload> =
        client.callResult {
            post(OpenUserSecurityRoutes.REGENERATE_RECOVERY_CODES)
        }
}