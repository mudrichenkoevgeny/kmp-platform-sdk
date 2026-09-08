package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * Loads auth settings and exposes which sign-in providers the backend allows for this app.
 *
 * For [AppType.CLIENT], it fetches dynamic settings from [OpenAuthSettingsRepository].
 * For [AppType.MANAGEMENT], it currently returns a hardcoded list (EMAIL only).
 */
class GetAvailableUserAuthProvidersUseCase(
    private val appType: AppType,
    private val openAuthSettingsRepository: OpenAuthSettingsRepository? = null
) {
    /**
     * @return [AvailableAuthProviders] allowed for the current application type.
     */
    suspend operator fun invoke(): AppResult<AvailableAuthProviders> {
        return when (appType) {
            AppType.CLIENT -> {
                val repository = openAuthSettingsRepository
                    ?: return AppResult.Error(
                        CommonError.ContractViolation(
                            throwable = IllegalStateException("OpenAuthSettingsRepository is required for AppType.CLIENT")
                        )
                    )

                repository.getOpenAuthSettings().mapSuccess { authSettings ->
                    authSettings.availableAuthProviders
                }
            }
            AppType.MANAGEMENT -> {
                AppResult.Success(
                    AvailableAuthProviders(
                        primary = listOf(UserAuthProvider.EMAIL),
                        secondary = emptyList()
                    )
                )
            }
        }
    }
}
