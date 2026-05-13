package io.github.mudrichenkoevgeny.kmp.core.common.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.naming.ClientCommonErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import org.jetbrains.compose.resources.stringResource

/**
 * Fallback [AppErrorParser] for common error codes.
 *
 * This parser handles standard system-wide error codes defined in [CommonErrorCodes]
 * and client-specific codes from [ClientCommonErrorCodes].
 *
 * It automatically resolves localized messages with dynamic arguments (e.g., field names,
 * retry intervals) if they are present in the error context. If an expected argument
 * is missing, it falls back to a generic version of the message to ensure a clean UI.
 */
object CommonErrorParser : AppErrorParser {

    @Composable
    override fun parse(appError: AppError): String {
        val args = appError.args ?: emptyMap()

        return when (appError.code) {
            CommonErrorCodes.NOT_FOUND -> resolveLocalizedString(
                args = args,
                key = CommonErrorArgs.RESOURCE,
                withArgsRes = Res.string.error_common_not_found_args,
                fallbackRes = Res.string.error_common_not_found
            )

            CommonErrorCodes.MISSING_REQUIRED_PARAMETER -> resolveLocalizedString(
                args = args,
                key = CommonErrorArgs.PARAMETER_NAME,
                withArgsRes = Res.string.error_common_missing_parameter_args,
                fallbackRes = Res.string.error_common_missing_parameter
            )

            CommonErrorCodes.INVALID_PARAMETER_VALUE -> resolveLocalizedString(
                args = args,
                key = CommonErrorArgs.PARAMETER_NAME,
                withArgsRes = Res.string.error_common_invalid_parameter_args,
                fallbackRes = Res.string.error_common_invalid_parameter
            )

            CommonErrorCodes.MISSING_REQUIRED_FIELD -> resolveLocalizedString(
                args = args,
                key = CommonErrorArgs.FIELD_NAME,
                withArgsRes = Res.string.error_common_missing_field_args,
                fallbackRes = Res.string.error_common_missing_field
            )

            CommonErrorCodes.BLANK_STRING_FIELD -> resolveLocalizedString(
                args = args,
                key = CommonErrorArgs.FIELD_NAME,
                withArgsRes = Res.string.error_common_blank_field_args,
                fallbackRes = Res.string.error_common_blank_field
            )

            CommonErrorCodes.EMPTY_COLLECTION_FIELD -> resolveLocalizedString(
                args = args,
                key = CommonErrorArgs.FIELD_NAME,
                withArgsRes = Res.string.error_common_empty_field_args,
                fallbackRes = Res.string.error_common_empty_field
            )

            CommonErrorCodes.INVALID_FIELD_VALUE -> resolveLocalizedString(
                args = args,
                key = CommonErrorArgs.FIELD_NAME,
                withArgsRes = Res.string.error_common_invalid_field_args,
                fallbackRes = Res.string.error_common_invalid_field
            )

            CommonErrorCodes.TOO_MANY_REQUESTS -> resolveLocalizedString(
                args = args,
                key = CommonErrorArgs.RETRY_AFTER_SECONDS,
                withArgsRes = Res.string.error_common_too_many_requests_args,
                fallbackRes = Res.string.error_common_too_many_requests
            )

            CommonErrorCodes.SERVICE_UNAVAILABLE ->
                stringResource(Res.string.error_common_service_unavailable)

            ClientCommonErrorCodes.NO_INTERNET_CONNECTION ->
                stringResource(Res.string.error_common_no_internet)

            ClientCommonErrorCodes.NETWORK ->
                stringResource(Res.string.error_common_network)

            CommonErrorCodes.INTERNAL,
            CommonErrorCodes.BAD_REQUEST,
            CommonErrorCodes.UNKNOWN,
            ClientCommonErrorCodes.CONTRACT_VIOLATION,
            ClientCommonErrorCodes.LIFECYCLE_ERROR ->
                stringResource(Res.string.error_common_internal)

            else -> stringResource(Res.string.error_common_unknown)
        }
    }
}