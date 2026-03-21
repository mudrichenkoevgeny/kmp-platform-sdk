package io.github.mudrichenkoevgeny.kmp.sample.app.usecase

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.RefreshAuthSettingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
 * Runs global, security, and auth settings refresh use cases concurrently and logs failures without throwing.
 *
 * @param refreshGlobalSettingsUseCase Settings module refresh.
 * @param refreshSecuritySettingsUseCase Security module refresh.
 * @param refreshAuthSettingsUseCase User auth settings refresh.
 */
class SyncDataUseCase(
    private val refreshGlobalSettingsUseCase: RefreshGlobalSettingsUseCase,
    private val refreshSecuritySettingsUseCase: RefreshSecuritySettingsUseCase,
    private val refreshAuthSettingsUseCase: RefreshAuthSettingsUseCase
) {
    /**
     * Awaits all three refresh jobs; errors are logged and swallowed.
     */
    suspend operator fun invoke(): Unit = withContext(Dispatchers.Default) {
        val tasks = listOf(
            async { refreshGlobalSettingsUseCase().logIfError("GlobalSettings") },
            async { refreshSecuritySettingsUseCase().logIfError("SecuritySettings") },
            async { refreshAuthSettingsUseCase().logIfError("AuthSettings") }
        )

        tasks.awaitAll()
    }

    private fun <T> AppResult<T>.logIfError(tag: String): AppResult<T> {
        if (this is AppResult.Error) {
            Logger.e { "Failed to sync $tag: ${this.error}" }
        }
        return this
    }
}