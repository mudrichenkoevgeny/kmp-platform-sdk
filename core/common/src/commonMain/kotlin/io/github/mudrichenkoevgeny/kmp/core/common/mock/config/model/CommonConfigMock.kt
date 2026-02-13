package io.github.mudrichenkoevgeny.kmp.core.common.mock.config.model

import io.github.mudrichenkoevgeny.kmp.core.common.config.model.CommonConfig

fun mockCommonConfig(
    baseUrl: String = "https://api.mock.com",
    appVersion: String = "1.0.0-mock"
) = CommonConfig(
    baseUrl = baseUrl,
    appVersion = appVersion
)