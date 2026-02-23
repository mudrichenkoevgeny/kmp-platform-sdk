package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings.KtorGlobalSettingsApi
import io.ktor.client.HttpClient

internal class SettingsNetworkModule(httpClient: HttpClient) {
    val globalSettingsApi by lazy {
        KtorGlobalSettingsApi(
            httpClient
        )
    }
}