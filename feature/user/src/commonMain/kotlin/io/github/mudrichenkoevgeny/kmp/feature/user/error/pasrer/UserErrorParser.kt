package io.github.mudrichenkoevgeny.kmp.feature.user.error.pasrer

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.CommonErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes
import org.jetbrains.compose.resources.stringResource

object UserErrorParser : AppErrorParser {
    @Composable
    override fun parse(appError: AppError): String? = when (appError.code) {
        UserErrorCodes.INVALID_ACCESS_TOKEN,
        UserErrorCodes.ACCESS_TOKEN_EXPIRED,
        UserErrorCodes.INVALID_REFRESH_TOKEN,
        UserErrorCodes.INVALID_SESSION,
        UserErrorCodes.EXTERNAL_ID_MISMATCH,
        UserErrorCodes.EXTERNAL_TOKEN_INVALID ->
            stringResource(Res.string.error_user_auth_failed)

        UserErrorCodes.USER_BLOCKED ->
            stringResource(Res.string.error_user_blocked)

        UserErrorCodes.USER_READ_ONLY ->
            stringResource(Res.string.error_user_read_only)

        UserErrorCodes.USER_FORBIDDEN ->
            stringResource(Res.string.error_user_access_denied)

        UserErrorCodes.USER_NOT_FOUND ->
            stringResource(Res.string.error_user_not_found)

        UserErrorCodes.INVALID_CREDENTIALS ->
            stringResource(Res.string.error_user_invalid_credentials)

        UserErrorCodes.WRONG_PASSWORD ->
            stringResource(Res.string.error_user_wrong_password)

        UserErrorCodes.WRONG_CONFIRMATION_CODE ->
            stringResource(Res.string.error_user_wrong_confirmation_code)

        UserErrorCodes.ALREADY_HAS_USER_IDENTIFIER_WITH_THAT_TYPE ->
            stringResource(Res.string.error_user_identifier_already_exists)

        UserErrorCodes.CAN_NOT_DELETE_USER_IDENTIFIER,
        UserErrorCodes.CAN_NOT_CREATE_USER_IDENTIFIER ->
            stringResource(CommonRes.string.error_common_internal)

        else -> CommonErrorParser.parse(appError)
    }
}