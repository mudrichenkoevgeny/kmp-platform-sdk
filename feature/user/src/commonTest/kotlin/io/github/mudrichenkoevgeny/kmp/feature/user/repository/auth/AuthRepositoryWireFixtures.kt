package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.AuthDataResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AuthSettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AvailableAuthProvidersResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.token.SessionTokenResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.user.CurrentUserResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.useridentifier.UserIdentifierResponse

internal const val WIRE_UUID_USER = "123e4567-e89b-12d3-a456-426614174000"
internal const val WIRE_UUID_IDENTIFIER = "223e4567-e89b-12d3-a456-426614174001"

internal const val WIRE_DEFAULT_SESSION_ACCESS_TOKEN = "access-token"
internal const val WIRE_DEFAULT_SESSION_REFRESH_TOKEN = "refresh-token"
internal const val WIRE_DEFAULT_SESSION_EXPIRES_AT = 99L
internal const val WIRE_DEFAULT_TOKEN_TYPE = "Bearer"
internal const val WIRE_DEFAULT_CURRENT_USER_CREATED_AT_MS = 1L
internal const val WIRE_DEFAULT_USER_IDENTIFIER_EMAIL = "user@example.com"
internal const val WIRE_DEFAULT_USER_IDENTIFIER_CREATED_AT = 0L

internal fun wireSessionTokenResponse(
    accessToken: String = WIRE_DEFAULT_SESSION_ACCESS_TOKEN,
    refreshToken: String = WIRE_DEFAULT_SESSION_REFRESH_TOKEN,
    expiresAt: Long = WIRE_DEFAULT_SESSION_EXPIRES_AT,
    tokenType: String = WIRE_DEFAULT_TOKEN_TYPE
): SessionTokenResponse = SessionTokenResponse(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresAt = expiresAt,
    tokenType = tokenType
)

internal fun wireCurrentUserResponse(
    id: String = WIRE_UUID_USER,
    createdAtEpochMs: Long = WIRE_DEFAULT_CURRENT_USER_CREATED_AT_MS
): CurrentUserResponse = CurrentUserResponse(
    id = id,
    role = UserRole.USER.wireValue,
    accountStatus = UserAccountStatus.ACTIVE.wireValue,
    lastLoginAt = null,
    lastActiveAt = null,
    createdAt = createdAtEpochMs,
    updatedAt = null
)

private val UserRole.wireValue: String
    get() = name

private val UserAccountStatus.wireValue: String
    get() = name

internal fun wireAuthDataResponse(): AuthDataResponse = AuthDataResponse(
    currentUserResponse = wireCurrentUserResponse(),
    sessionTokenResponse = wireSessionTokenResponse()
)

internal fun wireSendConfirmationResponse(retryAfterSeconds: Int = 0): SendConfirmationResponse =
    SendConfirmationResponse(retryAfterSeconds = retryAfterSeconds)

internal fun wireUserIdentifierResponse(
    identifier: String = WIRE_DEFAULT_USER_IDENTIFIER_EMAIL
): UserIdentifierResponse = UserIdentifierResponse(
    id = WIRE_UUID_IDENTIFIER,
    userId = WIRE_UUID_USER,
    userAuthProvider = UserAuthProvider.EMAIL.serialName,
    identifier = identifier,
    createdAt = WIRE_DEFAULT_USER_IDENTIFIER_CREATED_AT,
    updatedAt = null
)

internal fun wireAuthSettingsResponse(): AuthSettingsResponse = AuthSettingsResponse(
    availableAuthProviders = AvailableAuthProvidersResponse(
        primary = listOf(UserAuthProvider.EMAIL.serialName),
        secondary = emptyList()
    )
)
