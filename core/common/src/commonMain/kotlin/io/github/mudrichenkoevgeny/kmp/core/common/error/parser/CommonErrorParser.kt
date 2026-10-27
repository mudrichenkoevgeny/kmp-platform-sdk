package io.github.mudrichenkoevgeny.kmp.core.common.error.parser

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.client.naming.ClientCommonErrorCodes
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonModuleStrings

/**
 * Fallback [AppErrorParser] for common error codes using `shared-foundation` localization.
 */
open class CommonErrorParser(
    private val localeProvider: () -> String = { KmpLocalization.currentLocale }
) : AppErrorParser {

    companion object : CommonErrorParser()

    override fun parse(appError: AppError): String {
        val args = appError.args ?: emptyMap()
        val strings = CommonModuleStrings(KmpLocalization.resolver, localeProvider())

        return when (appError.code) {
            CommonErrorCodes.NOT_FOUND -> {
                val resource = args[CommonErrorArgs.RESOURCE]
                if (!resource.isNullOrBlank()) {
                    strings.errors.notFound(resource)
                } else {
                    strings.errors.notFoundWithoutArgs
                }
            }

            CommonErrorCodes.MISSING_REQUIRED_PARAMETER -> {
                val param = args[CommonErrorArgs.PARAMETER_NAME]
                if (!param.isNullOrBlank()) {
                    strings.errors.missingField(param)
                } else {
                    strings.errors.missingFieldWithoutArgs
                }
            }

            CommonErrorCodes.INVALID_PARAMETER_VALUE -> {
                val param = args[CommonErrorArgs.PARAMETER_NAME]
                if (!param.isNullOrBlank()) {
                    strings.errors.invalidField(param)
                } else {
                    strings.errors.invalidFieldWithoutArgs
                }
            }

            CommonErrorCodes.MISSING_REQUIRED_FIELD,
            CommonErrorCodes.EMPTY_COLLECTION_FIELD -> {
                val field = args[CommonErrorArgs.FIELD_NAME]
                if (!field.isNullOrBlank()) {
                    strings.errors.missingField(field)
                } else {
                    strings.errors.missingFieldWithoutArgs
                }
            }

            CommonErrorCodes.BLANK_STRING_FIELD -> {
                val field = args[CommonErrorArgs.FIELD_NAME]
                if (!field.isNullOrBlank()) {
                    strings.errors.blankField(field)
                } else {
                    strings.errors.blankFieldWithoutArgs
                }
            }

            CommonErrorCodes.INVALID_FIELD_VALUE -> {
                val field = args[CommonErrorArgs.FIELD_NAME]
                if (!field.isNullOrBlank()) {
                    strings.errors.invalidField(field)
                } else {
                    strings.errors.invalidFieldWithoutArgs
                }
            }

            CommonErrorCodes.TOO_MANY_REQUESTS -> {
                val retry = args[CommonErrorArgs.RETRY_AFTER_SECONDS]
                if (!retry.isNullOrBlank()) {
                    strings.errors.tooManyRequests(retry)
                } else {
                    strings.errors.tooManyRequestsWithoutArgs
                }
            }

            CommonErrorCodes.SERVICE_UNAVAILABLE -> strings.errors.serviceUnavailable

            ClientCommonErrorCodes.NO_INTERNET_CONNECTION -> strings.errors.noInternet

            ClientCommonErrorCodes.NETWORK -> strings.errors.network

            CommonErrorCodes.INTERNAL,
            CommonErrorCodes.BAD_REQUEST,
            CommonErrorCodes.INVALID_JSON_BODY,
            CommonErrorCodes.UNKNOWN,
            ClientCommonErrorCodes.CONTRACT_VIOLATION,
            ClientCommonErrorCodes.LIFECYCLE_ERROR -> strings.errors.internal

            else -> strings.errors.unknown
        }
    }
}
