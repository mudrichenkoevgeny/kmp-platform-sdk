package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.security.UserSecurityApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.totprecoverycodes.toTotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.totpsetup.toTotpSetup
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload

class SelfManagementUserSecurityRepositoryImpl(
    private val userSecurityApi: UserSecurityApi,
    private val userStorage: UserStorage
) : UserSecurityRepository {

    override suspend fun setupTotp(): AppResult<TotpSetup> {
        return userSecurityApi.setupTotp().mapSuccess { totpSetupPayload ->
            totpSetupPayload.toTotpSetup()
        }
    }

    override suspend fun enableTotp(mfaToken: String, code: String): AppResult<TotpRecoveryCodes> {
        val request = VerifyTotpPayload(
            mfaToken = mfaToken,
            code = code
        )
        return userSecurityApi.enableTotp(request).mapSuccess { totpRecoveryCodesPayload ->
            updateTotpStatusInStorage(isEnabled = true)
            totpRecoveryCodesPayload.toTotpRecoveryCodes()
        }
    }

    override suspend fun disableTotp(): AppResult<Unit> {
        return userSecurityApi.disableTotp().mapSuccess {
            updateTotpStatusInStorage(isEnabled = false)
        }
    }

    override suspend fun getRecoveryCodes(): AppResult<TotpRecoveryCodes> {
        return userSecurityApi.getRecoveryCodes().mapSuccess { totpRecoveryCodesPayload ->
            totpRecoveryCodesPayload.toTotpRecoveryCodes()
        }
    }

    override suspend fun regenerateRecoveryCodes(): AppResult<TotpRecoveryCodes> {
        return userSecurityApi.regenerateRecoveryCodes().mapSuccess { totpRecoveryCodesPayload ->
            totpRecoveryCodesPayload.toTotpRecoveryCodes()
        }
    }

    private suspend fun updateTotpStatusInStorage(isEnabled: Boolean) {
        val currentUser = userStorage.getCurrentUser()
        if (currentUser != null) {
            userStorage.updateCurrentUser(
                currentUser.copy(isTotpEnabled = isEnabled)
            )
        }
    }
}