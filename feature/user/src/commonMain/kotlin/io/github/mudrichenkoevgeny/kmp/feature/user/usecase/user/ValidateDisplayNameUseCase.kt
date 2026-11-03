package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.flatMap
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAuthSettingsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.validator.displayname.DisplayNameValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.validator.displayname.DisplayNameValidatorResult

/**
 * Validates a user's display name against remote authentication settings constraints and character rules.
 *
 * @param displayNameValidator Validator engine for display names.
 * @param getAuthSettingsUseCase Use case to retrieve current public auth settings.
 */
open class ValidateDisplayNameUseCase(
    private val displayNameValidator: DisplayNameValidator,
    private val getAuthSettingsUseCase: GetAuthSettingsUseCase
) {
    /**
     * Validates [newDisplayName] against constraints.
     *
     * @param newDisplayName The target display name input.
     * @return [AppResult.Success] if valid, or [AppResult.Error] wrapping [UserError] on failure.
     */
    open suspend operator fun invoke(newDisplayName: String): AppResult<Unit> {
        return getAuthSettingsUseCase().flatMap { authSettings ->
            val minLength = authSettings.userDisplayNameMinLength
            val maxLength = authSettings.userDisplayNameMaxLength

            val validationResult = displayNameValidator.validate(
                displayName = newDisplayName,
                minLength = minLength,
                maxLength = maxLength
            )
            
            when (validationResult) {
                is DisplayNameValidatorResult.Fail -> {
                    val error = when (validationResult.errorCode) {
                        UserErrorCodes.DISPLAY_NAME_TOO_SHORT -> UserError.DisplayNameTooShort(minLength)
                        UserErrorCodes.DISPLAY_NAME_TOO_LONG -> UserError.DisplayNameTooLong(maxLength)
                        UserErrorCodes.INVALID_DISPLAY_NAME_CHARACTERS -> UserError.InvalidDisplayNameCharacters()
                        else -> UserError.InvalidDisplayNameCharacters()
                    }
                    AppResult.Error(error)
                }
                is DisplayNameValidatorResult.Success -> {
                    AppResult.Success(Unit)
                }
            }
        }
    }
}
