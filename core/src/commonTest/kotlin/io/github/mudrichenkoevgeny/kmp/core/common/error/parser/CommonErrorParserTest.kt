package io.github.mudrichenkoevgeny.kmp.core.common.error.parser

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.client.naming.ClientCommonErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorCodes
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CommonErrorParserTest {

    private val parser = CommonErrorParser()
    private val unknownFallbackMessage = parser.parse(createTestAppError("CUSTOM_UNHANDLED_CODE"))

    @Test
    fun parse_notFound_withResourceArg_interpolatesArgumentAndDiffersFromUnknown() {
        val appError = createTestAppError(
            code = CommonErrorCodes.NOT_FOUND,
            args = mapOf(CommonErrorArgs.RESOURCE to "User")
        )

        val parsed = parser.parse(appError)
        assertNotNull(parsed)
        assertTrue(
            parsed.contains("User"),
            "Parsed string must contain resource argument 'User'"
        )
        assertNotEquals(unknownFallbackMessage, parsed)
    }

    @Test
    fun parse_notFound_withoutArgs() {
        val appError = createTestAppError(code = CommonErrorCodes.NOT_FOUND)

        val parsed = parser.parse(appError)
        assertNotNull(parsed)
        assertTrue(parsed.isNotBlank())
        assertNotEquals(unknownFallbackMessage, parsed)
    }

    @Test
    fun parse_missingParameter_withArg_interpolatesArgumentAndDiffersFromUnknown() {
        val appError = createTestAppError(
            code = CommonErrorCodes.MISSING_REQUIRED_PARAMETER,
            args = mapOf(CommonErrorArgs.PARAMETER_NAME to "userId")
        )

        val parsed = parser.parse(appError)
        assertNotNull(parsed)
        assertTrue(
            parsed.contains("userId"),
            "Parsed string must contain parameter argument 'userId'"
        )
        assertNotEquals(unknownFallbackMessage, parsed)
    }

    @Test
    fun parse_tooManyRequests_withRetryArg_interpolatesArgumentAndDiffersFromUnknown() {
        val appError = createTestAppError(
            code = CommonErrorCodes.TOO_MANY_REQUESTS,
            args = mapOf(CommonErrorArgs.RETRY_AFTER_SECONDS to "60")
        )

        val parsed = parser.parse(appError)
        assertNotNull(parsed)
        assertTrue(parsed.contains("60"), "Parsed string must contain retry argument '60'")
        assertNotEquals(unknownFallbackMessage, parsed)
    }

    @Test
    fun parse_noInternetConnection_differsFromUnknownMessage() {
        val appError = createTestAppError(code = ClientCommonErrorCodes.NO_INTERNET_CONNECTION)

        val parsed = parser.parse(appError)
        assertNotNull(parsed)
        assertTrue(parsed.isNotBlank())
        assertNotEquals(unknownFallbackMessage, parsed)
    }

    @Test
    fun parse_unknownCode_fallsBackToDefaultUnknownMessage() {
        val appError = createTestAppError(code = "CUSTOM_UNKNOWN_CODE_999")

        val parsed = parser.parse(appError)
        assertNotNull(parsed)
        assertTrue(parsed.isNotBlank())
    }

    @Test
    fun chainOfResponsibility_delegatesToFeatureParserFirst_andCascadesToCommonParserOnUnhandledCode() {
        val featureParser = object : AppErrorParser {
            override fun parse(appError: AppError): String? = if (appError.code == "FEATURE_SPECIFIC_ERROR") {
                "Handled by Feature Parser"
            } else {
                null
            }
        }

        val chainedParser = object : AppErrorParser {
            override fun parse(appError: AppError): String = featureParser.parse(appError) ?: parser.parse(appError)
        }

        val featureError = createTestAppError(code = "FEATURE_SPECIFIC_ERROR")
        val parsedFeatureError = chainedParser.parse(featureError)
        assertTrue(parsedFeatureError.contains("Handled by Feature Parser"))

        val commonError = createTestAppError(
            code = CommonErrorCodes.NOT_FOUND,
            args = mapOf(CommonErrorArgs.RESOURCE to "User")
        )
        val parsedCommonError = chainedParser.parse(commonError)
        assertTrue(parsedCommonError.contains("User"))
        assertNotEquals("Handled by Feature Parser", parsedCommonError)
    }

    private fun createTestAppError(
        code: String,
        args: Map<String, String>? = null
    ): AppError = object : AppError {
        override val id: ErrorId = ErrorId.generate()
        override val code: String = code
        override val args: Map<String, String>? = args
        override val isRetryable: Boolean = false
    }
}
