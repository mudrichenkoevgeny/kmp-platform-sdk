package io.github.mudrichenkoevgeny.kmp.core.security.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.resolveLocalizedString
import io.github.mudrichenkoevgeny.kmp.core.security.Res
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.ClientSecurityErrorCodes
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_invalid_mfa_token
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_invalid_totp_code
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_ip_not_allowed
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_mfa_confirmation_required
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_mfa_confirmation_required_args
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_mfa_token_expired
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_otp_retry_too_soon
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_otp_retry_too_soon_args
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_no_digit
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_no_letter
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_no_lowercase
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_no_special_char
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_no_uppercase
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_policy_unavailable
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_too_common
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_too_short
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_too_short_args
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_too_weak
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_password_too_weak_args
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_recovery_code_used
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_totp_already_enabled
import io.github.mudrichenkoevgeny.kmp.core.security.error_security_totp_not_enabled
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorCodes
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
            SecurityErrorCodes.MFA_CONFIRMATION_REQUIRED -> resolveLocalizedString(
                args = args,
                key = SecurityErrorArgs.MFA_TOKEN,
                withArgsRes = Res.string.error_security_mfa_confirmation_required_args,
                fallbackRes = Res.string.error_security_mfa_confirmation_required
            )

            SecurityErrorCodes.OTP_RETRY_TOO_SOON -> resolveLocalizedString(
                args = args,
                key = CommonErrorArgs.RETRY_AFTER_SECONDS,
                withArgsRes = Res.string.error_security_otp_retry_too_soon_args,
                fallbackRes = Res.string.error_security_otp_retry_too_soon
            )

            SecurityErrorCodes.PASSWORD_TOO_WEAK -> {
                if (args.isEmpty())
                    return stringResource(Res.string.error_security_password_too_weak)

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

            SecurityErrorCodes.IP_NOT_ALLOWED ->
                stringResource(Res.string.error_security_ip_not_allowed)

            ClientSecurityErrorCodes.PASSWORD_POLICY_UNAVAILABLE ->
                stringResource(Res.string.error_security_password_policy_unavailable)

            ClientSecurityErrorCodes.PASSWORD_TOO_SHORT -> resolveLocalizedString(
                args = args,
                key = SecurityErrorArgs.PASSWORD_MIN_LENGTH,
                withArgsRes = Res.string.error_security_password_too_short_args,
                fallbackRes = Res.string.error_security_password_too_short
            )

            ClientSecurityErrorCodes.PASSWORD_NO_LETTER ->
                stringResource(Res.string.error_security_password_no_letter)

            ClientSecurityErrorCodes.PASSWORD_NO_UPPERCASE ->
                stringResource(Res.string.error_security_password_no_uppercase)

            ClientSecurityErrorCodes.PASSWORD_NO_LOWERCASE ->
                stringResource(Res.string.error_security_password_no_lowercase)

            ClientSecurityErrorCodes.PASSWORD_NO_DIGIT ->
                stringResource(Res.string.error_security_password_no_digit)

            ClientSecurityErrorCodes.PASSWORD_NO_SPECIAL_CHAR ->
                stringResource(Res.string.error_security_password_no_special_char)

            ClientSecurityErrorCodes.PASSWORD_TOO_COMMON ->
                stringResource(Res.string.error_security_password_too_common)

            else -> null
        }
    }
}
