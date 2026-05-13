package io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlin.time.Clock

@InternalApi
fun userIdentifierMock() = UserIdentifier(
    userId = UserId.generate(),
    userAuthProvider = UserAuthProvider.EMAIL,
    identifier = "user@example.com",
    externalProviderEmail = null,
    isSensitiveValuesMasked = false,
    createdAt = Clock.System.now(),
    updatedAt = null
)