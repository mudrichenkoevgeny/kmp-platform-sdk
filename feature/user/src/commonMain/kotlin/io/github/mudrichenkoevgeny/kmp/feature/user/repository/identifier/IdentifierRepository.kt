package io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues

/**
 * Manages user identity identifiers, facilitating retrieval, pagination,
 * identifier lifecycle management, and security operations.
 */
interface IdentifierRepository {

    /**
     * Retrieves specific identifier details by its unique id.
     *
     * @param userIdentifierId Unique identifier payload id.
     * @return Detailed identifier info or a mapped failure.
     */
    suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifier>

    /**
     * Returns a paginated and filtered list of identifiers.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by.
     * @param sortOrder Sorting direction.
     * @param userAuthProviders Filters by specific provider types.
     * @param identifiers Filters by substring patterns of identifier values.
     * @return Paginated result containing matching user identifier models, or a mapped failure.
     */
    suspend fun getUserIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder? = null,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifier>>

    /**
     * Removes an existing identifier from the user profile.
     *
     * @param identifierId Unique identifier id to delete.
     * @return Success unit, or a mapped failure.
     */
    suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit>

    /**
     * Associates a new email identifier with the account.
     */
    suspend fun addUserIdentifierEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifier>

    /**
     * Associates a new phone number identifier with the account.
     */
    suspend fun addUserIdentifierPhone(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifier>

    /**
     * Associates an external authentication provider identifier (e.g., OAuth).
     */
    suspend fun addUserIdentifierExternalAuthProvider(
        authProvider: String,
        token: String
    ): AppResult<UserIdentifier>

    /**
     * Triggers a confirmation challenge for email association.
     */
    suspend fun sendAddEmailIdentifierConfirmation(
        email: String
    ): AppResult<OtpConfirmation>

    /**
     * Triggers a confirmation challenge for phone association.
     */
    suspend fun sendAddPhoneIdentifierConfirmation(
        phoneNumber: String
    ): AppResult<OtpConfirmation>

    /**
     * Updates the account password using current credentials.
     *
     * @param email Account email address.
     * @param oldPassword Current valid password.
     * @param newPassword Target password to be applied.
     * @return Success indicator, or a mapped failure.
     */
    suspend fun emailChangePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit>

    /**
     * Returns the remaining cooldown for email confirmation requests.
     */
    fun getRemainingEmailConfirmationDelayInSeconds(email: String): Int

    /**
     * Returns the remaining cooldown for phone confirmation requests.
     */
    fun getRemainingPhoneNumberConfirmationDelayInSeconds(phoneNumber: String): Int
}