package io.github.mudrichenkoevgeny.kmp.core.security.error.parser

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.ClientSecurityErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.localization.SecurityModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.localization.resolvePasswordTooWeakReasons

/**
 * [AppErrorParser] for security-related error codes using `shared-foundation` localization.
 */
open class SecurityErrorParser(
    private val localeProvider: () -> String = { KmpLocalization.currentLocale }
) : AppErrorParser {

    companion object : SecurityErrorParser()

    override fun parse(appError: AppError): String? {
        val args = appError.args ?: emptyMap()
        val strings = SecurityModuleStrings(KmpLocalization.resolver, localeProvider())

        return when (appError.code) {
            SecurityErrorCodes.MFA_CONFIRMATION_REQUIRED -> strings.errors.mfaConfirmationRequired

            SecurityErrorCodes.OTP_RETRY_TOO_SOON -> {
                val retry = args[CommonErrorArgs.RETRY_AFTER_SECONDS]
                if (!retry.isNullOrBlank()) {
                    strings.errors.otpRetryTooSoon(retry)
                } else {
                    strings.errors.otpRetryTooSoonWithoutArgs
                }
            }

            SecurityErrorCodes.PASSWORD_TOO_WEAK -> {
                strings.resolvePasswordTooWeakReasons(args).joinToString("\n")
            }

            SecurityErrorCodes.TOTP_ALREADY_ENABLED -> strings.errors.totpAlreadyEnabled

            SecurityErrorCodes.TOTP_NOT_ENABLED -> strings.errors.totpNotEnabled

            SecurityErrorCodes.MFA_TOKEN_EXPIRED,
            SecurityErrorCodes.INVALID_MFA_TOKEN -> strings.errors.mfaTokenExpired

            SecurityErrorCodes.RECOVERY_CODE_ALREADY_USED -> strings.errors.recoveryCodeAlreadyUsed

            SecurityErrorCodes.INVALID_TOTP_CODE -> strings.errors.invalidTotpCode

            SecurityErrorCodes.IP_NOT_ALLOWED -> strings.errors.ipNotAllowed

            ClientSecurityErrorCodes.PASSWORD_POLICY_UNAVAILABLE -> strings.errors.passwordPolicyUnavailable

            ClientSecurityErrorCodes.PASSWORD_TOO_SHORT -> {
                val minLength = args[SecurityErrorArgs.PASSWORD_MIN_LENGTH]
                if (!minLength.isNullOrBlank()) {
                    strings.errors.passwordTooShort(minLength)
                } else {
                    strings.errors.passwordTooShortWithoutArgs
                }
            }

            ClientSecurityErrorCodes.PASSWORD_NO_LETTER -> strings.errors.passwordNoLetter

            ClientSecurityErrorCodes.PASSWORD_NO_UPPERCASE -> strings.errors.passwordNoUppercase

            ClientSecurityErrorCodes.PASSWORD_NO_LOWERCASE -> strings.errors.passwordNoLowercase

            ClientSecurityErrorCodes.PASSWORD_NO_DIGIT -> strings.errors.passwordNoDigit

            ClientSecurityErrorCodes.PASSWORD_NO_SPECIAL_CHAR -> strings.errors.passwordNoSpecialChar

            ClientSecurityErrorCodes.PASSWORD_TOO_COMMON -> strings.errors.passwordTooCommon

            else -> null
        }
    }
}
