package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.identifier.OpenIdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
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
    private val confirmationRepository: ConfirmationRepository,
    private val userStorage: UserStorage
) : IdentifierRepository {

    override suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifier> {
        return openIdentifiersApi.getUserIdentifier(userIdentifierId)
            .mapSuccess { it.toUserIdentifier() }
    }

    override suspend fun getUserIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifier>> {
        return openIdentifiersApi.getUserIdentifiers(
            pageNumber, pageSize, sortBy, sortOrder, userAuthProviders, identifiers
        ).mapSuccess { pagedPayload ->
            pagedPayload.mapItems { userIdentifierPayload ->
                userIdentifierPayload.toUserIdentifier()
            }
        }
    }

    override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> {
        return openIdentifiersApi.deleteUserIdentifier(identifierId).mapSuccess {
            userStorage.removeUserIdentifier(identifierId)
        }
    }

    override suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return openIdentifiersApi.addUserIdentifierEmail(
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
        return openIdentifiersApi.addUserIdentifierPhone(
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
        return openIdentifiersApi.addUserIdentifierExternalAuthProvider(
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
        email: String,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        return openIdentifiersApi.emailChangePassword(
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