package io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.identifier.IdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierPhoneRequest

/**
 * Implements [IdentifierRepository] using [IdentifiersApi] and [ConfirmationRepository] for throttled
 * email and phone confirmation sends.
 *
 * @param identifiersApi HTTP endpoints for managing user identifiers and sending confirmations.
 * @param confirmationRepository Client-side cooldown for adding email and phone identifiers.
 */
class IdentifierRepositoryImpl(
    private val identifiersApi: IdentifiersApi,
    private val confirmationRepository: ConfirmationRepository,
    private val userStorage: UserStorage
) : IdentifierRepository {

    override suspend fun getUserIdentifiers(): AppResult<PagedResult<UserIdentifier>> {
        return identifiersApi.getUserIdentifiers().mapSuccess { pagedPayload ->
            userStorage.updateUserIdentifiersPayloadList(pagedPayload)
            pagedPayload.mapItems { userIdentifierPayload ->
                userIdentifierPayload.toUserIdentifier()
            }
        }
    }

    override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> {
        return identifiersApi.deleteUserIdentifier(identifierId).mapSuccess {
            userStorage.removeUserIdentifier(identifierId)
        }
    }

    override suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return identifiersApi.addUserIdentifierEmail(
            AddUserIdentifierEmailRequest(email, password, confirmationCode)
        ).mapSuccess { payload ->
            val identifier = payload.toUserIdentifier()
            userStorage.addUserIdentifier(identifier)
            identifier
        }
    }

    override suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return identifiersApi.addUserIdentifierPhone(
            AddUserIdentifierPhoneRequest(phoneNumber, confirmationCode)
        ).mapSuccess { payload ->
            val identifier = payload.toUserIdentifier()
            userStorage.addUserIdentifier(identifier)
            identifier
        }
    }

    override suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        token: String
    ): AppResult<UserIdentifier> {
        return identifiersApi.addUserIdentifierExternalAuthProvider(
            AddUserIdentifierExternalAuthProviderRequest(authProvider, token)
        ).mapSuccess { payload ->
            val identifier = payload.toUserIdentifier()
            userStorage.addUserIdentifier(identifier)
            identifier
        }
    }

    override suspend fun sendAddEmailIdentifierConfirmation(
        email: String
    ): AppResult<OtpConfirmation> {
        return confirmationRepository.executeWithTimer(
            type = ConfirmationType.ADD_EMAIL,
            identifier = email
        ) {
            identifiersApi.sendAddEmailIdentifierConfirmation(
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
            identifiersApi.sendAddPhoneIdentifierConfirmation(
                SendConfirmationToPhoneRequest(phoneNumber)
            ).mapSuccess { it.toOtpConfirmation() }
        }
    }

    override suspend fun emailChangePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        return identifiersApi.emailChangePassword(
            EmailPasswordChangeRequest(email, oldPassword, newPassword)
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