package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.validation

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.flatMap
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAuthSettingsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.validator.nickname.NicknameValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.validator.nickname.NicknameValidatorResult

/**
 * Validates a user's nickname against remote authentication settings constraints, policy rules, and character set.
 *
 * @param nicknameValidator Validator engine for nicknames.
 * @param getAuthSettingsUseCase Use case to retrieve current public auth settings.
 */
open class ValidateNicknameUseCase(
    private val nicknameValidator: NicknameValidator,
    private val getAuthSettingsUseCase: GetAuthSettingsUseCase
) {
    /**
     * Validates [newNickname] against constraints and policy.
     *
     * @param newNickname The target nickname input.
     * @param currentNickname The existing nickname of the user, if any.
     * @return [AppResult.Success] if valid, or [AppResult.Error] wrapping [UserError] on failure.
     */
    open suspend operator fun invoke(
        newNickname: String,
        currentNickname: String?
    ): AppResult<Unit> {
        return getAuthSettingsUseCase().flatMap { authSettings ->
            val minLength = authSettings.userNicknameMinLength
            val maxLength = authSettings.userNicknameMaxLength
            val isChangeAllowed = authSettings.isUserNicknameChangeAllowed

            val validationResult = nicknameValidator.validate(
                nickname = newNickname,
                minLength = minLength,
                maxLength = maxLength,
                isNicknameChangeAllowed = isChangeAllowed,
                isNicknameChanging = currentNickname != null && currentNickname != newNickname
            )
            
            when (validationResult) {
                is NicknameValidatorResult.Fail -> {
                    val error = when (validationResult.errorCode) {
                        UserErrorCodes.NICKNAME_CHANGE_DISABLED -> UserError.NicknameChangeDisabled()
                        UserErrorCodes.NICKNAME_TOO_SHORT -> UserError.NicknameTooShort(minLength)
                        UserErrorCodes.NICKNAME_TOO_LONG -> UserError.NicknameTooLong(maxLength)
                        UserErrorCodes.INVALID_NICKNAME_CHARACTERS -> UserError.InvalidNicknameCharacters()
                        else -> UserError.InvalidNicknameCharacters()
                    }
                    AppResult.Error(error)
                }
                is NicknameValidatorResult.Success -> {
                    AppResult.Success(Unit)
                }
            }
        }
    }
}
