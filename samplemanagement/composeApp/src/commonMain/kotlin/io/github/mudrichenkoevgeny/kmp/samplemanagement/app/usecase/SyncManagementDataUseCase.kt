package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.usecase

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.RefreshManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.RefreshManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.RefreshManagementSecuritySettingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
 * Runs global, security, and auth settings refresh use cases concurrently and logs failures without throwing.
 *
 * @param refreshManagementGlobalSettingsUseCase Settings module refresh.
 * @param refreshManagementSecuritySettingsUseCase Security module refresh.
 * @param refreshManagementAuthSettingsUseCase User auth settings refresh.
 */
class SyncManagementDataUseCase(
    private val refreshManagementGlobalSettingsUseCase: RefreshManagementGlobalSettingsUseCase,
    private val refreshManagementSecuritySettingsUseCase: RefreshManagementSecuritySettingsUseCase,
    private val refreshManagementAuthSettingsUseCase: RefreshManagementAuthSettingsUseCase
) {
    /**
     * Awaits all three refresh jobs; errors are logged and swallowed.
     */
    suspend operator fun invoke(): Unit = withContext(Dispatchers.Default) {
        val tasks = listOf(
            async { refreshManagementGlobalSettingsUseCase().logIfError("GlobalSettings") },
            async { refreshManagementSecuritySettingsUseCase().logIfError("SecuritySettings") },
            async { refreshManagementAuthSettingsUseCase().logIfError("AuthSettings") }
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