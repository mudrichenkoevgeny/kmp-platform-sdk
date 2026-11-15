package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.otpconfirmation.createTestOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary

/**
 * Mock implementation of [IdentifierRepository] for testing.
 */
@InternalApi
public open class IdentifierRepositoryMock : IdentifierRepository {

    public var getUserIdentifierResultProvider: (UserIdentifierId) -> AppResult<UserIdentifierPrivate> = {
        AppResult.Success(createTestUserIdentifierPrivate())
    }

    public var getUserIdentifiersResultProvider: () -> AppResult<PagedResult<UserIdentifierSummary>> = {
        AppResult.Success(createTestPagedResult(listOf(createTestUserIdentifierSummary())))
    }

    public var deleteUserIdentifierResultProvider: (UserIdentifierId) -> AppResult<Unit> = { _ ->
        AppResult.Success(Unit)
    }

    public var addUserIdentifierResultProvider: () -> AppResult<UserIdentifierPrivate> = {
        AppResult.Success(createTestUserIdentifierPrivate())
    }

    public var otpConfirmationResultProvider: () -> AppResult<OtpConfirmation> = {
        AppResult.Success(createTestOtpConfirmation())
    }

    public var emailChangePasswordResultProvider: () -> AppResult<Unit> = { AppResult.Success(Unit) }

    public var lastIdentifierId: UserIdentifierId? = null
    public var lastEmail: String? = null
    public var lastPassword: String? = null
    public var lastConfirmationCode: String? = null
    public var lastPhoneNumber: String? = null
    public var lastAuthProvider: String? = null
    public var lastToken: String? = null
    public var lastOldPassword: String? = null
    public var lastNewPassword: String? = null

    public override suspend fun getUserIdentifier(
        userIdentifierId: UserIdentifierId
    ): AppResult<UserIdentifierPrivate> {
        lastIdentifierId = userIdentifierId
        return getUserIdentifierResultProvider(userIdentifierId)
    }

    public override suspend fun getUserIdentifiers(
        params: SelfIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummary>> = getUserIdentifiersResultProvider()

    public override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> {
        lastIdentifierId = identifierId
        return deleteUserIdentifierResultProvider(identifierId)
    }

    public override suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        lastEmail = email
        lastPassword = password
        lastConfirmationCode = confirmationCode
        return addUserIdentifierResultProvider()
    }

    public override suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        lastPhoneNumber = phoneNumber
        lastConfirmationCode = confirmationCode
        return addUserIdentifierResultProvider()
    }

    public override suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<UserIdentifierPrivate> {
        lastAuthProvider = authProvider
        lastToken = externalProviderToken
        return addUserIdentifierResultProvider()
    }

    public override suspend fun sendAddEmailIdentifierConfirmation(email: String): AppResult<OtpConfirmation> {
        lastEmail = email
        return otpConfirmationResultProvider()
    }

    public override suspend fun sendAddPhoneIdentifierConfirmation(phoneNumber: String): AppResult<OtpConfirmation> {
        lastPhoneNumber = phoneNumber
        return otpConfirmationResultProvider()
    }

    public override suspend fun emailChangePassword(
        userIdentifierId: UserIdentifierId,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        lastIdentifierId = userIdentifierId
        lastOldPassword = oldPassword
        lastNewPassword = newPassword
        return emailChangePasswordResultProvider()
    }

    public override fun getRemainingEmailConfirmationDelayInSeconds(email: String): Int = 0

    public override fun getRemainingPhoneNumberConfirmationDelayInSeconds(phoneNumber: String): Int = 0
}
