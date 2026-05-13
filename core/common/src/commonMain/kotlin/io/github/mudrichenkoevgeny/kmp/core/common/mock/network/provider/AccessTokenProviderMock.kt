package io.github.mudrichenkoevgeny.kmp.core.common.mock.network.provider

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory test double for [AccessTokenProvider].
 *
 * @param token Initial access token value exposed via [accessTokenFlow].
 */
@InternalApi
class AccessTokenProviderMock(
    token: String? = "mock_access_token"
) : AccessTokenProvider {
    override val accessTokenFlow: StateFlow<String?> = MutableStateFlow(token).asStateFlow()
}