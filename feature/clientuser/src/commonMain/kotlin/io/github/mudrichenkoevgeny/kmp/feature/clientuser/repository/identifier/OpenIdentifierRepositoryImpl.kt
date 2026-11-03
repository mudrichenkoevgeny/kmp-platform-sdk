package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.identifier.OpenIdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierPhoneRequest

/**
 * Implements [IdentifierRepository] using [OpenIdentifiersApi] and [ConfirmationRepository] for throttled
 * email and phone confirmation sends.
 *
 * @param openIdentifiersApi HTTP endpoints for managing user identifiers and sending confirmations.
 * @param confirmationRepository Client-side cooldown for adding email and phone identifiers.
 */
class OpenIdentifierRepositoryImpl(
    private val openIdentifiersApi: OpenIdentifiersApi,
    private val confirmationRepository: ConfirmationRepository
) : IdentifierRepository {

    override suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPrivate> {
        return openIdentifiersApi.getUserIdentifier(userIdentifierId)
            .mapSuccess { it.toUserIdentifierPrivate() }
    }

    override suspend fun getUserIdentifiers(
        params: SelfIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        return openIdentifiersApi.getUserIdentifiers(params).mapSuccess { pagedPayload ->
            pagedPayload.mapItems { userIdentifierPayload ->
                userIdentifierPayload.toUserIdentifierSummary()
            }
        }
    }

    override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> {
        return openIdentifiersApi.deleteUserIdentifier(identifierId)
    }

    override suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        return openIdentifiersApi.addUserIdentifierEmail(
            AddUserIdentifierEmailRequest(email, password, confirmationCode)
        ).mapSuccess { it.toUserIdentifierPrivate() }
    }

    override suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        return openIdentifiersApi.addUserIdentifierPhone(
            AddUserIdentifierPhoneRequest(phoneNumber, confirmationCode)
        ).mapSuccess { it.toUserIdentifierPrivate() }
    }

    override suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<UserIdentifierPrivate> {
        return openIdentifiersApi.addUserIdentifierExternalAuthProvider(
            AddUserIdentifierExternalAuthProviderRequest(authProvider, externalProviderToken)
        ).mapSuccess { it.toUserIdentifierPrivate() }
    }

    override suspend fun sendAddEmailIdentifierConfirmation(
        email: String
    ): AppResult<OtpConfirmation> {
        return confirmationRepository.executeWithTimer(
            type = ConfirmationType.ADD_EMAIL,
            identifier = email
        ) {
            openIdentifiersApi.sendAddEmailIdentifierConfirmation(
                SendConfirmationToEmailRequest(email)
            ).mapSuccess { it.toOtpConfirmation() }
        }
    }

    override suspend fun sendAddPhoneIdentifierConfirmation(
        phoneNumber: String
    ): AppResult<OtpConfirmation> {
        return confirmationRepository.executeWithTimer(
            type = ConfirmationType.ADD_PHONE,
            identifier = phoneNumber
        ) {
            openIdentifiersApi.sendAddPhoneIdentifierConfirmation(
                SendConfirmationToPhoneRequest(phoneNumber)
            ).mapSuccess { it.toOtpConfirmation() }
        }
    }

    override suspend fun emailChangePassword(
        userIdentifierId: UserIdentifierId,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        return openIdentifiersApi.emailChangePassword(
            userIdentifierId = userIdentifierId,
            request = EmailPasswordChangeRequest(oldPassword, newPassword)
        )
    }

    override fun getRemainingEmailConfirmationDelayInSeconds(email: String): Int {
        return confirmationRepository.getRemainingDelay(
            type = ConfirmationType.ADD_EMAIL,
            identifier = email
        )
    }

    override fun getRemainingPhoneNumberConfirmationDelayInSeconds(phoneNumber: String): Int {
        return confirmationRepository.getRemainingDelay(
            type = ConfirmationType.ADD_PHONE,
            identifier = phoneNumber
        )
    }
}
