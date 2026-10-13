package io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlin.time.Clock

@InternalApi
fun userIdentifierSummaryMock(
    id: UserIdentifierId = UserIdentifierId.generate(),
    userAuthProvider: UserAuthProvider = UserAuthProvider.EMAIL,
    identifier: String = "user@example.com",
    displayName: String = "user@example.com"
) = UserIdentifierSummary(
    id = id,
    userAuthProvider = userAuthProvider,
    identifier = identifier,
    displayName = displayName
)

@InternalApi
fun userIdentifierPrivateMock(
    id: UserIdentifierId = UserIdentifierId.generate(),
    userId: UserId = UserId.generate(),
    userAuthProvider: UserAuthProvider = UserAuthProvider.EMAIL,
    identifier: String = "user@example.com",
    displayName: String = "user@example.com",
    externalProviderEmail: String? = null,
    isSensitiveValuesMasked: Boolean = false
) = UserIdentifierPrivate(
    id = id,
    userId = userId,
    userAuthProvider = userAuthProvider,
    identifier = identifier,
    displayName = displayName,
    externalProviderEmail = externalProviderEmail,
    isSensitiveValuesMasked = isSensitiveValuesMasked,
    createdAt = Clock.System.now(),
    updatedAt = null
)

@InternalApi
fun userIdentifierMock() = userIdentifierSummaryMock()