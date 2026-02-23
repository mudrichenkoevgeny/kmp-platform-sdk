package io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ApiException
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.model.ApiErrorResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonHttpHeaders
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val DYNAMIC_HEADERS = "DynamicHeaders"
private const val WEB_SOCKETS_PING_INTERVAL_SECONDS = 30L
private const val LOGGER_RESPONSE_VALIDATOR_PREFIX = "Response validator"

@OptIn(ExperimentalUuidApi::class)
fun HttpClientConfig<*>.setupCommonConfig(
    baseUrl: String,
    networkLogger: Logger,
    deviceInfo: DeviceInfo
) {
    install(ContentNegotiation) {
        json(FoundationJson)
    }

    install(Logging) {
        level = LogLevel.ALL
        logger = networkLogger
    }

    install(DYNAMIC_HEADERS) {
        requestPipeline.intercept(HttpRequestPipeline.State) {
            val traceId = Uuid.random().toHexDashString()
            context.header(CommonHttpHeaders.TRACE_HEADER_NAME, traceId)
        }
    }

    install(WebSockets) {
        pingInterval = WEB_SOCKETS_PING_INTERVAL_SECONDS.seconds
    }

    defaultRequest {
        url(baseUrl)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        header(CommonHttpHeaders.CLIENT_TYPE_HEADER_NAME, deviceInfo.clientType)
        header(CommonHttpHeaders.DEVICE_ID_HEADER_NAME, deviceInfo.deviceId)
        header(CommonHttpHeaders.DEVICE_NAME_HEADER_NAME, deviceInfo.deviceName)
        header(CommonHttpHeaders.APP_VERSION_HEADER_NAME, deviceInfo.appVersion)
        header(CommonHttpHeaders.OPERATION_SYSTEM_VERSION_HEADER_NAME, deviceInfo.osVersion)
        header(HttpHeaders.AcceptLanguage, deviceInfo.language)
    }

    expectSuccess = true

    HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, request ->
            val responseException = exception as? ResponseException
                ?: return@handleResponseExceptionWithRequest

            val response = responseException.response
            val statusCode = response.status.value
            val requestUrl = request.url.toString()

            val apiErrorResponse = try {
                response.body<ApiErrorResponse>()
            } catch (e: Exception) {
                networkLogger.log("$LOGGER_RESPONSE_VALIDATOR_PREFIX: Failed to parse error response from $requestUrl: $e")
                null
            }

            if (apiErrorResponse != null) {
                networkLogger.log("$LOGGER_RESPONSE_VALIDATOR_PREFIX: API Error [Code: ${apiErrorResponse.code}] at $requestUrl. ID: ${apiErrorResponse.id}")
                throw ApiException(apiErrorResponse)
            } else {
                networkLogger.log("$LOGGER_RESPONSE_VALIDATOR_PREFIX: Generic HTTP Error [$statusCode] at $requestUrl")
            }
        }
    }
}