package io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

interface IdentifierRepository {

    suspend fun getUserIdentifiers(): AppResult<PagedResult<UserIdentifier>>

    suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit>

    suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifier>

    suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifier>

    suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        token: String
    ): AppResult<UserIdentifier>

    suspend fun sendAddEmailIdentifierConfirmation(
        email: String
    ): AppResult<OtpConfirmation>

    suspend fun sendAddPhoneIdentifierConfirmation(
        phoneNumber: String
    ): AppResult<OtpConfirmation>


    suspend fun emailChangePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit>

    fun getRemainingEmailConfirmationDelayInSeconds(email: String): Int

    fun getRemainingPhoneNumberConfirmationDelayInSeconds(phoneNumber: String): Int
}