package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.otpconfirmation.createTestOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary

@InternalApi
open class IdentifierRepositoryMock : IdentifierRepository {

    var getUserIdentifierResultProvider: (UserIdentifierId) -> AppResult<UserIdentifierPrivate> = {
        AppResult.Success(createTestUserIdentifierPrivate())
    }

    var getUserIdentifiersResultProvider: () -> AppResult<PagedResult<UserIdentifierSummary>> = {
        AppResult.Success(createTestPagedResult(listOf(createTestUserIdentifierSummary())))
    }

    var deleteUserIdentifierResultProvider: (UserIdentifierId) -> AppResult<Unit> = { _ -> AppResult.Success(Unit) }

    var addUserIdentifierResultProvider: () -> AppResult<UserIdentifierPrivate> = {
        AppResult.Success(createTestUserIdentifierPrivate())
    }

    var otpConfirmationResultProvider: () -> AppResult<OtpConfirmation> = {
        AppResult.Success(createTestOtpConfirmation())
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

    override suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPrivate> {
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
    ): AppResult<PagedResult<UserIdentifierSummary>> = getUserIdentifiersResultProvider()

    override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> {
        lastIdentifierId = identifierId
        return deleteUserIdentifierResultProvider(identifierId)
    }

    override suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        lastEmail = email
        lastPassword = password
        lastConfirmationCode = confirmationCode
        return addUserIdentifierResultProvider()
    }

    override suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        lastPhoneNumber = phoneNumber
        lastConfirmationCode = confirmationCode
        return addUserIdentifierResultProvider()
    }

    override suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<UserIdentifierPrivate> {
        lastAuthProvider = authProvider
        lastToken = externalProviderToken
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
        userIdentifierId: UserIdentifierId,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        lastIdentifierId = userIdentifierId
        lastOldPassword = oldPassword
        lastNewPassword = newPassword
        return emailChangePasswordResultProvider()
    }

    override fun getRemainingEmailConfirmationDelayInSeconds(email: String): Int = 0

    override fun getRemainingPhoneNumberConfirmationDelayInSeconds(phoneNumber: String): Int = 0
}