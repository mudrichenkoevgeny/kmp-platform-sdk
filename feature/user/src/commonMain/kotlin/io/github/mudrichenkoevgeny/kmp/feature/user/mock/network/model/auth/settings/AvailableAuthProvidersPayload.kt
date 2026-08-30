package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.AvailableAuthProvidersPayload

@InternalApi
fun availableAuthProvidersPayloadMock(
    primary: List<String> = listOf("email"),
    secondary: List<String> = listOf("google")
): AvailableAuthProvidersPayload = AvailableAuthProvidersPayload(
    primary = primary,
    secondary = secondary
)