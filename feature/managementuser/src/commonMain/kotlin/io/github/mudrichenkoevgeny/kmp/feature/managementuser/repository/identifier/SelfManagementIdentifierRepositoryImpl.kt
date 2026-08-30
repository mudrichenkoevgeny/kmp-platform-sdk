package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.SelfManagementIdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest

/**
 * Implements [IdentifierRepository] using [SelfManagementIdentifiersApi] for the management context.
 * Only identifier retrieval and password modification are supported.
 */
class SelfManagementIdentifierRepositoryImpl(
    private val selfManagementIdentifiersApi: SelfManagementIdentifiersApi
) : IdentifierRepository {

    override suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifier> {
        return selfManagementIdentifiersApi.getUserIdentifier(userIdentifierId)
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
        return selfManagementIdentifiersApi.getUserIdentifiers(
            pageNumber, pageSize, sortBy, sortOrder, userAuthProviders, identifiers
        ).mapSuccess { pagedPayload ->
            pagedPayload.mapItems { userIdentifierPayload ->
                userIdentifierPayload.toUserIdentifier()
            }
        }
    }

    override suspend fun emailChangePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        return selfManagementIdentifiersApi.emailChangePassword(
            EmailPasswordChangeRequest(email, oldPassword, newPassword)
        )
    }

    override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> {
        return methodNotSupported()
    }

    override suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return methodNotSupported()
    }

    override suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return methodNotSupported()
    }

    override suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        token: String
    ): AppResult<UserIdentifier> {
        return methodNotSupported()
    }

    override suspend fun sendAddEmailIdentifierConfirmation(email: String): AppResult<OtpConfirmation> {
        return methodNotSupported()
    }

    override suspend fun sendAddPhoneIdentifierConfirmation(phoneNumber: String): AppResult<OtpConfirmation> {
        return methodNotSupported()
    }

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