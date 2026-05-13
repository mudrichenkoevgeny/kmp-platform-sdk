package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload

@InternalApi
fun userIdentifierPayloadMock(
    identifier: String = "user@example.com"
): UserIdentifierPayload = UserIdentifierPayload(
    id = "223e4567-e89b-12d3-a456-426614174001",
    userId = "123e4567-e89b-12d3-a456-426614174000",
    userAuthProvider = UserAuthProvider.EMAIL.serialName,
    identifier = identifier,
    externalProviderEmail = null,
    isSensitiveValuesMasked = false,
    createdAt = 0L,
    updatedAt = null
)