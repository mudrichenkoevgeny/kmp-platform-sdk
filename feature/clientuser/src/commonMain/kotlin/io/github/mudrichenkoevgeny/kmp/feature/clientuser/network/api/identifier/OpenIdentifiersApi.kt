package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierPhoneRequest

/** Manage user identifiers (email, phone, external providers) and related confirmations. */
interface OpenIdentifiersApi {
    /**
     * Retrieves specific identifier details by its unique id.
     *
     * @param userIdentifierId Unique identifier payload id.
     * @return Detailed identifier info or a mapped failure.
     */
    suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPayload>

    /**
     * Returns a paginated and filtered list of identifiers linked to the current authenticated management account.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by (created_at, updated_at).
     * @param sortOrder Sorting direction (ASC, DESC).
     * @param userAuthProviders Filters by specific provider types.
     * @param identifiers Filters by substring patterns of identifier values.
     * @return Paginated result containing matching user identifier payloads, or a mapped failure.
     */
    suspend fun getUserIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder? = null,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifierPayload>>

    /**
     * Removes an identifier from the account.
     *
     * @param identifierId Server identifier of the user-identifier row to delete.
     * @return Success or a mapped failure.
     */
    suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit>

    /**
     * Starts linking a new email identifier to the account.
     *
     * @param request Email linkage payload from the shared contract.
     * @return Created or pending identifier row, or a mapped failure.
     */
    suspend fun addUserIdentifierEmail(
        request: AddUserIdentifierEmailRequest
    ): AppResult<UserIdentifierPayload>

    /**
     * Starts linking a new phone identifier to the account.
     *
     * @param request Phone linkage payload from the shared contract.
     * @return Created or pending identifier row, or a mapped failure.
     */
    suspend fun addUserIdentifierPhone(
        request: AddUserIdentifierPhoneRequest
    ): AppResult<UserIdentifierPayload>

    /**
     * Starts linking an external auth provider identity to the account.
     *
     * @param request Provider linkage payload from the shared contract.
     * @return Created or pending identifier row, or a mapped failure.
     */
    suspend fun addUserIdentifierExternalAuthProvider(
        request: AddUserIdentifierExternalAuthProviderRequest
    ): AppResult<UserIdentifierPayload>

    /**
     * Sends a confirmation message for adding an email identifier.
     *
     * @param request Target email and template parameters from the shared contract.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendAddEmailIdentifierConfirmation(
        request: SendConfirmationToEmailRequest
    ): AppResult<OtpConfirmationPayload>

    /**
     * Sends a confirmation message for adding a phone identifier.
     *
     * @param request Target phone and channel details from the shared contract.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendAddPhoneIdentifierConfirmation(
        request: SendConfirmationToPhoneRequest
    ): AppResult<OtpConfirmationPayload>


    /**
     * Changes the password for the signed-in user.
     *
     * @param request Current and new password payload from the shared contract.
     * @return Domain user identifier after the change, or a mapped failure.
     */
    suspend fun emailChangePassword(request: EmailPasswordChangeRequest): AppResult<Unit>
}