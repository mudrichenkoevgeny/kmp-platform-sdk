package io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams

/**
 * Manages user identity identifiers, facilitating retrieval, pagination,
 * identifier lifecycle management, and security operations.
 */
public interface IdentifierRepository {

    /**
     * Retrieves specific identifier details by its unique id.
     *
     * @param userIdentifierId Unique identifier payload id.
     * @return Detailed identifier info or a mapped failure.
     */
    public suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPrivate>

    /**
     * Returns a paginated and filtered list of identifiers.
     *
     * @param params Query and filter parameters DTO.
     * @return Paginated result containing matching user identifier models, or a mapped failure.
     */
    public suspend fun getUserIdentifiers(
        params: SelfIdentifierListParams? = null
    ): AppResult<PagedResult<UserIdentifierSummary>>

    /**
     * Removes an identifier from the account.
     *
     * @param identifierId Server identifier of the user-identifier row to delete.
     * @return Success or a mapped failure.
     */
    public suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit>

    /**
     * Starts linking a new email identifier to the account.
     *
     * @param email Target email address to link.
     * @param password Password for verification.
     * @param confirmationCode Verification OTP code.
     * @return Created or pending identifier row, or a mapped failure.
     */
    public suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate>

    /**
     * Starts linking a new phone identifier to the account.
     *
     * @param phoneNumber Target phone number to link.
     * @param confirmationCode Verification OTP code.
     * @return Created or pending identifier row, or a mapped failure.
     */
    public suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate>

    /**
     * Starts linking an external auth provider identity to the account.
     *
     * @param authProvider External provider identifier name.
     * @param externalProviderToken Access/ID token from the provider.
     * @return Created or pending identifier row, or a mapped failure.
     */
    public suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<UserIdentifierPrivate>

    /**
     * Sends a confirmation message for adding an email identifier.
     *
     * @param email Target email address to send OTP to.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    public suspend fun sendAddEmailIdentifierConfirmation(email: String): AppResult<OtpConfirmation>

    /**
     * Sends a confirmation message for adding a phone identifier.
     *
     * @param phoneNumber Target phone number to send OTP to.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    public suspend fun sendAddPhoneIdentifierConfirmation(phoneNumber: String): AppResult<OtpConfirmation>

    /**
     * Changes the password for the signed-in user's email identifier.
     *
     * @param userIdentifierId Target identifier ID.
     * @param oldPassword Current password.
     * @param newPassword New password to set.
     * @return Success indicator, or a mapped failure.
     */
    public suspend fun emailChangePassword(
        userIdentifierId: UserIdentifierId,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit>

    /**
     * Returns remaining delay in seconds before a new email confirmation OTP can be sent.
     */
    public fun getRemainingEmailConfirmationDelayInSeconds(email: String): Int

    /**
     * Returns remaining delay in seconds before a new phone confirmation OTP can be sent.
     */
    public fun getRemainingPhoneNumberConfirmationDelayInSeconds(phoneNumber: String): Int
}
