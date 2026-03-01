package io.github.mudrichenkoevgeny.kmp.core.common.network.provider

import kotlinx.coroutines.flow.StateFlow

interface AccessTokenProvider {
    val accessTokenFlow: StateFlow<String?>
}