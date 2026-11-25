package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.SelfManagementIdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest

/**
 * Implements [IdentifierRepository] using [SelfManagementIdentifiersApi] for the management context.
 * Only identifier retrieval and password modification are supported.
 */
public class SelfManagementIdentifierRepositoryImpl(
    private val selfManagementIdentifiersApi: SelfManagementIdentifiersApi
) : IdentifierRepository {

    override suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPrivate> =
        selfManagementIdentifiersApi.getUserIdentifier(
            userIdentifierId
        )
            .mapSuccess { it.toUserIdentifierPrivate() }

    override suspend fun getUserIdentifiers(
        params: SelfIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummary>> = selfManagementIdentifiersApi.getUserIdentifiers(
        params
    ).mapSuccess { pagedPayload ->
        pagedPayload.mapItems { userIdentifierPayload ->
            userIdentifierPayload.toUserIdentifierSummary()
        }
    }

    override suspend fun emailChangePassword(
        userIdentifierId: UserIdentifierId,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> = selfManagementIdentifiersApi.emailChangePassword(
        userIdentifierId = userIdentifierId,
        request = EmailPasswordChangeRequest(oldPassword, newPassword)
    )

    override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> = methodNotSupported()

    override suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> = methodNotSupported()

    override suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> = methodNotSupported()

    override suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<UserIdentifierPrivate> = methodNotSupported()

    override suspend fun sendAddEmailIdentifierConfirmation(email: String): AppResult<OtpConfirmation> =
        methodNotSupported()

    override suspend fun sendAddPhoneIdentifierConfirmation(phoneNumber: String): AppResult<OtpConfirmation> =
        methodNotSupported()

    override fun getRemainingEmailConfirmationDelayInSeconds(email: String): Int = 0

    override fun getRemainingPhoneNumberConfirmationDelayInSeconds(phoneNumber: String): Int = 0

    private fun <T> methodNotSupported(): AppResult<T> = AppResult.Error(
        CommonError.ContractViolation(
            throwable = IllegalStateException(
                "This identifier management method is not supported in management context."
            )
        )
    )
}
