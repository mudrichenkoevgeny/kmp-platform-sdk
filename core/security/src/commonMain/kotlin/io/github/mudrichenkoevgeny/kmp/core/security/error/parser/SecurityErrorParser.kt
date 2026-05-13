package io.github.mudrichenkoevgeny.kmp.core.security.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.resolveLocalizedString
import io.github.mudrichenkoevgeny.kmp.core.security.Res
import io.github.mudrichenkoevgeny.kmp.core.security.*
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorArgs
import org.jetbrains.compose.resources.stringResource

/**
 * [AppErrorParser] for security-related error codes.
 *
 * Handles Multi-Factor Authentication (MFA), TOTP, password policies,
 * and rate-limiting for security actions like OTP requests.
 */
object SecurityErrorParser : AppErrorParser {

    @Composable
    override fun parse(appError: AppError): String? {
        val args = appError.args ?: emptyMap()

        return when (appError.code) {
            SecurityErrorCodes.TOTP_CONFIRMATION_REQUIRED -> resolveLocalizedString(
                args = args,
                key = SecurityErrorArgs.MFA_TOKEN,
                withArgsRes = Res.string.error_security_totp_confirmation_required_args,
                fallbackRes = Res.string.error_security_totp_confirmation_required
            )

            SecurityErrorCodes.OTP_RETRY_TOO_SOON -> resolveLocalizedString(
                args = args,
                key = CommonErrorArgs.RETRY_AFTER_SECONDS,
                withArgsRes = Res.string.error_security_otp_retry_too_soon_args,
                fallbackRes = Res.string.error_security_otp_retry_too_soon
            )

            SecurityErrorCodes.PASSWORD_TOO_WEAK -> {
                if (args.isEmpty()) return stringResource(Res.string.error_security_password_too_weak)

                stringResource(
                    Res.string.error_security_password_too_weak_args,
                    args[SecurityErrorArgs.PASSWORD_FAIL_TOO_SHORT] ?: "false",
                    args[SecurityErrorArgs.PASSWORD_MIN_LENGTH] ?: "0",
                    args[SecurityErrorArgs.PASSWORD_FAIL_NO_LETTER] ?: "false",
                    args[SecurityErrorArgs.PASSWORD_FAIL_NO_UPPERCASE] ?: "false",
                    args[SecurityErrorArgs.PASSWORD_FAIL_NO_LOWERCASE] ?: "false",
                    args[SecurityErrorArgs.PASSWORD_FAIL_NO_DIGIT] ?: "false",
                    args[SecurityErrorArgs.PASSWORD_FAIL_NO_SPECIAL_CHAR] ?: "false",
                    args[SecurityErrorArgs.PASSWORD_FAIL_TOO_COMMON] ?: "false"
                )
            }

            SecurityErrorCodes.TOTP_ALREADY_ENABLED ->
                stringResource(Res.string.error_security_totp_already_enabled)

            SecurityErrorCodes.TOTP_NOT_ENABLED ->
                stringResource(Res.string.error_security_totp_not_enabled)

            SecurityErrorCodes.MFA_TOKEN_EXPIRED ->
                stringResource(Res.string.error_security_mfa_token_expired)

            SecurityErrorCodes.RECOVERY_CODE_ALREADY_USED ->
                stringResource(Res.string.error_security_recovery_code_used)

            SecurityErrorCodes.INVALID_TOTP_CODE ->
                stringResource(Res.string.error_security_invalid_totp_code)

            SecurityErrorCodes.INVALID_MFA_TOKEN ->
                stringResource(Res.string.error_security_invalid_mfa_token)

            else -> null
        }
    }
}