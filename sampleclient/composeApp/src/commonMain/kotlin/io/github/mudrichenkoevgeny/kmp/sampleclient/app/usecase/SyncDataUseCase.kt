package io.github.mudrichenkoevgeny.kmp.sampleclient.app.usecase

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshOpenSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshOpenGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.usecase.auth.settings.RefreshOpenAuthSettingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
 * Runs global, security, and auth settings refresh use cases concurrently and logs failures without throwing.
 *
 * @param refreshOpenGlobalSettingsUseCase Settings module refresh.
 * @param refreshOpenSecuritySettingsUseCase Security module refresh.
 * @param refreshOpenAuthSettingsUseCase User auth settings refresh.
 */
class SyncDataUseCase(
    private val refreshOpenGlobalSettingsUseCase: RefreshOpenGlobalSettingsUseCase,
    private val refreshOpenSecuritySettingsUseCase: RefreshOpenSecuritySettingsUseCase,
    private val refreshOpenAuthSettingsUseCase: RefreshOpenAuthSettingsUseCase
) {
    /**
     * Awaits all three refresh jobs; errors are logged and swallowed.
     */
    suspend operator fun invoke(): Unit = withContext(Dispatchers.Default) {
        val tasks = listOf(
            async { refreshOpenGlobalSettingsUseCase().logIfError("GlobalSettings") },
            async { refreshOpenSecuritySettingsUseCase().logIfError("SecuritySettings") },
            async { refreshOpenAuthSettingsUseCase().logIfError("AuthSettings") }
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