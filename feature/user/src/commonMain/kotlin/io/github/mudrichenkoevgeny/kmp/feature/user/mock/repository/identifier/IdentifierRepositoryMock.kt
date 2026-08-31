package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.otpConfirmationMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues

@InternalApi
open class IdentifierRepositoryMock : IdentifierRepository {

    var getUserIdentifierResultProvider: (UserIdentifierId) -> AppResult<UserIdentifier> = {
        AppResult.Success(userIdentifierMock())
    }

    var getUserIdentifiersResultProvider: () -> AppResult<PagedResult<UserIdentifier>> = {
        AppResult.Success(pagedResultMock(listOf(userIdentifierMock())))
    }

    var deleteUserIdentifierResultProvider: (UserIdentifierId) -> AppResult<Unit> = { _ -> AppResult.Success(Unit) }

    var addUserIdentifierResultProvider: () -> AppResult<UserIdentifier> = {
        AppResult.Success(userIdentifierMock())
    }

    var otpConfirmationResultProvider: () -> AppResult<OtpConfirmation> = {
        AppResult.Success(otpConfirmationMock())
    }

    var emailChangePasswordResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    var lastIdentifierId: UserIdentifierId? = null
    var lastEmail: String? = null
    var lastPassword: String? = null
    var lastConfirmationCode: String? = null
    var lastPhoneNumber: String? = null
    var lastAuthProvider: String? = null
    var lastToken: String? = null
    var lastOldPassword: String? = null
    var lastNewPassword: String? = null

    override suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifier> {
        lastIdentifierId = userIdentifierId
        return getUserIdentifierResultProvider(userIdentifierId)
    }

    override suspend fun getUserIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifier>> = getUserIdentifiersResultProvider()

    override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> {
        lastIdentifierId = identifierId
        return deleteUserIdentifierResultProvider(identifierId)
    }

    override suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        lastEmail = email
        lastPassword = password
        lastConfirmationCode = confirmationCode
        return addUserIdentifierResultProvider()
    }

    override suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        lastPhoneNumber = phoneNumber
        lastConfirmationCode = confirmationCode
        return addUserIdentifierResultProvider()
    }

    override suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        token: String
    ): AppResult<UserIdentifier> {
        lastAuthProvider = authProvider
        lastToken = token
        return addUserIdentifierResultProvider()
    }

    override suspend fun sendAddEmailIdentifierConfirmation(email: String): AppResult<OtpConfirmation> {
        lastEmail = email
        return otpConfirmationResultProvider()
    }

    override suspend fun sendAddPhoneIdentifierConfirmation(phoneNumber: String): AppResult<OtpConfirmation> {
        lastPhoneNumber = phoneNumber
        return otpConfirmationResultProvider()
    }

    override suspend fun emailChangePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        lastEmail = email
        lastOldPassword = oldPassword
        lastNewPassword = newPassword
        return emailChangePasswordResultProvider()
    }

    override fun getRemainingEmailConfirmationDelayInSeconds(email: String): Int = 0

    override fun getRemainingPhoneNumberConfirmationDelayInSeconds(phoneNumber: String): Int = 0
}
