package io.github.mudrichenkoevgeny.kmp.core.common.mock.network.provider

import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockAccessTokenProvider(
    token: String? = "mock_access_token"
) : AccessTokenProvider {
    override val accessTokenFlow: Flow<String?> = flowOf(token)
}