package io.github.mudrichenkoevgeny.kmp.core.common.mock.network.provider

import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

/**
 * In-memory test double for [AccessTokenProvider].
 *
 * @param token Initial access token value exposed via [accessTokenFlow].
 */
class MockAccessTokenProvider(
    token: String? = "mock_access_token"
) : AccessTokenProvider {
    override val accessTokenFlow: StateFlow<String?> = MutableStateFlow(token).asStateFlow()
}