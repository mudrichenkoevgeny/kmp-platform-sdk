package io.github.mudrichenkoevgeny.kmp.core.common.network.provider

import kotlinx.coroutines.flow.Flow

interface AccessTokenProvider {
    val accessTokenFlow: Flow<String?>
}