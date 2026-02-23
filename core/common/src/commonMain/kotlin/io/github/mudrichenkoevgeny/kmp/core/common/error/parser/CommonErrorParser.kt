package io.github.mudrichenkoevgeny.kmp.core.common.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.naming.ClientCommonErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorCodes
import org.jetbrains.compose.resources.stringResource

object CommonErrorParser : AppErrorParser {
    @Composable
    override fun parse(appError: AppError): String = when (appError.code) {
        CommonErrorCodes.UNKNOWN ->
            stringResource(Res.string.error_common_unknown)

        CommonErrorCodes.INTERNAL,
        CommonErrorCodes.BAD_REQUEST,
        CommonErrorCodes.INVALID_JSON_BODY,
        CommonErrorCodes.MISSING_REQUIRED_PARAMETER,
        CommonErrorCodes.INVALID_PARAMETER_VALUE,
        CommonErrorCodes.MISSING_REQUIRED_FIELD,
        CommonErrorCodes.BLANK_STRING_FIELD,
        CommonErrorCodes.EMPTY_COLLECTION_FIELD,
        CommonErrorCodes.INVALID_FIELD_VALUE,
        ClientCommonErrorCodes.CONTRACT_VIOLATION,
        ClientCommonErrorCodes.LIFECYCLE_ERROR ->
            stringResource(Res.string.error_common_internal)

        ClientCommonErrorCodes.NO_INTERNET_CONNECTION ->
            stringResource(Res.string.error_common_no_internet)

        ClientCommonErrorCodes.NETWORK ->
            stringResource(Res.string.error_common_network)

        CommonErrorCodes.SERVICE_UNAVAILABLE ->
            stringResource(Res.string.error_common_service_unavailable)

        CommonErrorCodes.TOO_MANY_REQUESTS ->
            stringResource(Res.string.error_common_too_many_requests)

        else -> stringResource(Res.string.error_common_unknown)
    }
}