package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.identifier.OpenIdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.confirmation.ConfirmationType
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
public class OpenIdentifierRepositoryImpl(
    private val openIdentifiersApi: OpenIdentifiersApi,
    private val confirmationRepository: ConfirmationRepository
) : IdentifierRepository {

    public override suspend fun getUserIdentifier(
        userIdentifierId: UserIdentifierId
    ): AppResult<UserIdentifierPrivate> = openIdentifiersApi.getUserIdentifier(userIdentifierId)
        .mapSuccess { it.toUserIdentifierPrivate() }

    public override suspend fun getUserIdentifiers(
        params: SelfIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummary>> = openIdentifiersApi.getUserIdentifiers(
        params
    ).mapSuccess { pagedPayload ->
        pagedPayload.mapItems { userIdentifierPayload ->
            userIdentifierPayload.toUserIdentifierSummary()
        }
    }

    public override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> =
        openIdentifiersApi.deleteUserIdentifier(
            identifierId
        )

    public override suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> = openIdentifiersApi.addUserIdentifierEmail(
        AddUserIdentifierEmailRequest(email, password, confirmationCode)
    ).mapSuccess { it.toUserIdentifierPrivate() }

    public override suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> = openIdentifiersApi.addUserIdentifierPhone(
        AddUserIdentifierPhoneRequest(phoneNumber, confirmationCode)
    ).mapSuccess { it.toUserIdentifierPrivate() }

    public override suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<UserIdentifierPrivate> = openIdentifiersApi.addUserIdentifierExternalAuthProvider(
        AddUserIdentifierExternalAuthProviderRequest(authProvider, externalProviderToken)
    ).mapSuccess { it.toUserIdentifierPrivate() }

    public override suspend fun sendAddEmailIdentifierConfirmation(
        email: String
    ): AppResult<OtpConfirmation> = confirmationRepository.executeWithTimer(
        type = ConfirmationType.ADD_EMAIL,
        identifier = email
    ) {
        openIdentifiersApi.sendAddEmailIdentifierConfirmation(
            SendConfirmationToEmailRequest(email)
        ).mapSuccess { it.toOtpConfirmation() }
    }

    public override suspend fun sendAddPhoneIdentifierConfirmation(
        phoneNumber: String
    ): AppResult<OtpConfirmation> = confirmationRepository.executeWithTimer(
        type = ConfirmationType.ADD_PHONE,
        identifier = phoneNumber
    ) {
        openIdentifiersApi.sendAddPhoneIdentifierConfirmation(
            SendConfirmationToPhoneRequest(phoneNumber)
        ).mapSuccess { it.toOtpConfirmation() }
    }

    public override suspend fun emailChangePassword(
        userIdentifierId: UserIdentifierId,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> = openIdentifiersApi.emailChangePassword(
        userIdentifierId = userIdentifierId,
        request = EmailPasswordChangeRequest(oldPassword, newPassword)
    )

    public override fun getRemainingEmailConfirmationDelayInSeconds(email: String): Int =
        confirmationRepository.getRemainingDelay(
            type = ConfirmationType.ADD_EMAIL,
            identifier = email
        )

    public override fun getRemainingPhoneNumberConfirmationDelayInSeconds(phoneNumber: String): Int =
        confirmationRepository.getRemainingDelay(
            type = ConfirmationType.ADD_PHONE,
            identifier = phoneNumber
        )
}
