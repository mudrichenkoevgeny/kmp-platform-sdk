package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload

@InternalApi
fun userIdentifierSummaryPayloadMock(
    id: String = "223e4567-e89b-12d3-a456-426614174001",
    userId: String = "123e4567-e89b-12d3-a456-426614174000",
    userAuthProvider: String = UserAuthProvider.EMAIL.value,
    identifier: String = "user@example.com",
    displayName: String = identifier
): UserIdentifierSummaryPayload = UserIdentifierSummaryPayload(
    id = id,
    userId = userId,
    userAuthProvider = userAuthProvider,
    identifier = identifier,
    displayName = displayName
)

@InternalApi
fun userIdentifierPrivatePayloadMock(
    id: String = "223e4567-e89b-12d3-a456-426614174001",
    userId: String = "123e4567-e89b-12d3-a456-426614174000",
    userAuthProvider: String = UserAuthProvider.EMAIL.value,
    identifier: String = "user@example.com",
    displayName: String = identifier
): UserIdentifierPrivatePayload = UserIdentifierPrivatePayload(
    id = id,
    userId = userId,
    userAuthProvider = userAuthProvider,
    identifier = identifier,
    displayName = displayName,
    externalProviderEmail = null,
    isSensitiveValuesMasked = false,
    createdAt = 0L,
    updatedAt = null
)

@InternalApi
fun userIdentifierPayloadMock(
    identifier: String = "user@example.com"
): UserIdentifierSummaryPayload = userIdentifierSummaryPayloadMock(identifier = identifier)