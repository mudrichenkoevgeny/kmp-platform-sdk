package io.github.mudrichenkoevgeny.kmp.core.security.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.CommonErrorParser
import io.github.mudrichenkoevgeny.kmp.core.security.Res
import io.github.mudrichenkoevgeny.kmp.core.security.*
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.SecurityErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorCodes as SharedSecurityErrorCodes
import org.jetbrains.compose.resources.stringResource

object SecurityErrorParser : AppErrorParser {
    @Composable
    override fun parse(appError: AppError): String? = when (appError.code) {
        SharedSecurityErrorCodes.PASSWORD_TOO_WEAK ->
            stringResource(Res.string.error_security_password_too_weak)

        SecurityErrorCodes.PASSWORD_POLICY_UNAVAILABLE ->
            stringResource(Res.string.error_security_password_policy_unavailable)

        SecurityErrorCodes.PASSWORD_TOO_SHORT ->
            stringResource(Res.string.error_security_password_too_short)

        SecurityErrorCodes.PASSWORD_NO_LETTER ->
            stringResource(Res.string.error_security_password_no_letter)

        SecurityErrorCodes.PASSWORD_NO_UPPERCASE ->
            stringResource(Res.string.error_security_password_no_uppercase)

        SecurityErrorCodes.PASSWORD_NO_LOWERCASE ->
            stringResource(Res.string.error_security_password_no_lowercase)

        SecurityErrorCodes.PASSWORD_NO_DIGIT ->
            stringResource(Res.string.error_security_password_no_digit)

        SecurityErrorCodes.PASSWORD_NO_SPECIAL_CHAR ->
            stringResource(Res.string.error_security_password_no_special_char)

        SecurityErrorCodes.PASSWORD_TOO_COMMON ->
            stringResource(Res.string.error_security_password_too_common)

        SharedSecurityErrorCodes.AUTHENTICATION_CONFIRMATION_REQUIRED ->
            stringResource(Res.string.error_security_confirmation_required)

        else -> CommonErrorParser.parse(appError)
    }
}