package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.token.sessionTokenPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.user.userDetailsPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload

@InternalApi
fun authDataPayloadMock(): AuthDataPayload = AuthDataPayload(
    userDetailsPayload = userDetailsPayloadMock(),
    sessionTokenPayload = sessionTokenPayloadMock()
)