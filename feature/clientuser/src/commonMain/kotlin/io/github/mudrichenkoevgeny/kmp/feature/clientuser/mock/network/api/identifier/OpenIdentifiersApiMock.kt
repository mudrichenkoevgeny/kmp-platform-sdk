package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.identifier.OpenIdentifiersApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierPhoneRequest

@InternalApi
open class OpenIdentifiersApiMock : OpenIdentifiersApi {

    var getUserIdentifierResult: AppResult<UserIdentifierPrivatePayload> = AppResult.Error(CommonError.Unknown())
    var getUserIdentifiersResult: AppResult<PagedResult<UserIdentifierSummaryPayload>> = AppResult.Success(PagedResult(emptyList(), 0, 1, 20, 0))
    var deleteUserIdentifierResult: AppResult<Unit> = AppResult.Success(Unit)
    var addUserIdentifierEmailResult: AppResult<UserIdentifierPrivatePayload> = AppResult.Error(CommonError.Unknown())
    var addUserIdentifierPhoneResult: AppResult<UserIdentifierPrivatePayload> = AppResult.Error(CommonError.Unknown())
    var addUserIdentifierExternalAuthProviderResult: AppResult<UserIdentifierPrivatePayload> = AppResult.Error(CommonError.Unknown())
    var sendAddEmailIdentifierConfirmationResult: AppResult<OtpConfirmationPayload> = AppResult.Error(CommonError.Unknown())
    var sendAddPhoneIdentifierConfirmationResult: AppResult<OtpConfirmationPayload> = AppResult.Error(CommonError.Unknown())
    var emailChangePasswordResult: AppResult<Unit> = AppResult.Success(Unit)

    override suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPrivatePayload> = getUserIdentifierResult

    override suspend fun getUserIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>> = getUserIdentifiersResult

    override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> = deleteUserIdentifierResult

    override suspend fun addUserIdentifierEmail(request: AddUserIdentifierEmailRequest): AppResult<UserIdentifierPrivatePayload> = addUserIdentifierEmailResult

    override suspend fun addUserIdentifierPhone(request: AddUserIdentifierPhoneRequest): AppResult<UserIdentifierPrivatePayload> = addUserIdentifierPhoneResult

    override suspend fun addUserIdentifierExternalAuthProvider(request: AddUserIdentifierExternalAuthProviderRequest): AppResult<UserIdentifierPrivatePayload> = addUserIdentifierExternalAuthProviderResult

    override suspend fun sendAddEmailIdentifierConfirmation(request: SendConfirmationToEmailRequest): AppResult<OtpConfirmationPayload> = sendAddEmailIdentifierConfirmationResult

    override suspend fun sendAddPhoneIdentifierConfirmation(request: SendConfirmationToPhoneRequest): AppResult<OtpConfirmationPayload> = sendAddPhoneIdentifierConfirmationResult

    override suspend fun emailChangePassword(request: EmailPasswordChangeRequest): AppResult<Unit> = emailChangePasswordResult
}