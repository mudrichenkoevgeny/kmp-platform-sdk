package io.github.mudrichenkoevgeny.kmp.core.security.error.parser

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicyFailReason
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.client.naming.ClientSecurityErrorCodes
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
                val reasons = strings.resolvePasswordTooWeakReasons(args)
                if (reasons.isNotEmpty()) {
                    reasons.joinToString("\n")
                } else {
                    strings.errors.passwordTooWeak
                }
            }

            SecurityErrorCodes.TOTP_ALREADY_ENABLED -> strings.errors.totpAlreadyEnabled

            SecurityErrorCodes.TOTP_NOT_ENABLED -> strings.errors.totpNotEnabled

            SecurityErrorCodes.MFA_TOKEN_EXPIRED,
            SecurityErrorCodes.INVALID_MFA_TOKEN -> strings.errors.mfaTokenExpired

            SecurityErrorCodes.RECOVERY_CODE_ALREADY_USED -> strings.errors.recoveryCodeAlreadyUsed

            SecurityErrorCodes.INVALID_TOTP_CODE -> strings.errors.invalidTotpCode

            SecurityErrorCodes.IP_NOT_ALLOWED -> strings.errors.ipNotAllowed

            ClientSecurityErrorCodes.PASSWORD_POLICY_UNAVAILABLE -> strings.errors.passwordPolicyUnavailable

            PasswordPolicyFailReason.TOO_SHORT.name -> {
                val minLength = args[SecurityErrorArgs.PASSWORD_MIN_LENGTH]
                if (!minLength.isNullOrBlank()) {
                    strings.errors.passwordTooShort(minLength)
                } else {
                    strings.errors.passwordTooShortWithoutArgs
                }
            }

            PasswordPolicyFailReason.NO_LETTER.name -> strings.errors.passwordNoLetter

            PasswordPolicyFailReason.NO_UPPERCASE.name -> strings.errors.passwordNoUppercase

            PasswordPolicyFailReason.NO_LOWERCASE.name -> strings.errors.passwordNoLowercase

            PasswordPolicyFailReason.NO_DIGIT.name -> strings.errors.passwordNoDigit

            PasswordPolicyFailReason.NO_SPECIAL_CHAR.name -> strings.errors.passwordNoSpecialChar

            PasswordPolicyFailReason.TOO_COMMON.name -> strings.errors.passwordTooCommon

            else -> null
        }
    }
}
