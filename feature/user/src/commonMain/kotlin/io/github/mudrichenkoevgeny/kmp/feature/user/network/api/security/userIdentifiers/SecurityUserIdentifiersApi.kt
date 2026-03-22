package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.security.userIdentifiers

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.useridentifier.UserIdentifierResponse

/** Manage user identifiers (email, phone, external providers) and related confirmations. */
interface SecurityUserIdentifiersApi {
    /**
     * Lists identifiers (email, phone, external accounts) linked to the user.
     *
     * @return Identifier rows from the shared contract, or a mapped failure.
     */
    suspend fun getUserIdentifiers(): AppResult<List<UserIdentifierResponse>>

    /**
     * Removes an identifier from the account.
     *
     * @param id Server identifier of the user-identifier row to delete.
     * @return Success or a mapped failure.
     */
    suspend fun deleteUserIdentifier(id: String): AppResult<Unit>

    /**
     * Starts linking a new email identifier to the account.
     *
     * @param request Email linkage payload from the shared contract.
     * @return Created or pending identifier row, or a mapped failure.
     */
    suspend fun addUserIdentifierEmail(
        request: AddUserIdentifierEmailRequest
    ): AppResult<UserIdentifierResponse>

    /**
     * Starts linking a new phone identifier to the account.
     *
     * @param request Phone linkage payload from the shared contract.
     * @return Created or pending identifier row, or a mapped failure.
     */
    suspend fun addUserIdentifierPhone(
        request: AddUserIdentifierPhoneRequest
    ): AppResult<UserIdentifierResponse>

    /**
     * Starts linking an external auth provider identity to the account.
     *
     * @param request Provider linkage payload from the shared contract.
     * @return Created or pending identifier row, or a mapped failure.
     */
    suspend fun addUserIdentifierExternalAuthProvider(
        request: AddUserIdentifierExternalAuthProviderRequest
    ): AppResult<UserIdentifierResponse>

    /**
     * Sends a confirmation message for adding an email identifier.
     *
     * @param request Target email and template parameters from the shared contract.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendAddEmailIdentifierConfirmation(
        request: SendConfirmationToEmailRequest
    ): AppResult<SendConfirmationResponse>

    /**
     * Sends a confirmation message for adding a phone identifier.
     *
     * @param request Target phone and channel details from the shared contract.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendAddPhoneIdentifierConfirmation(
        request: SendConfirmationToPhoneRequest
    ): AppResult<SendConfirmationResponse>
}