package io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ApiException
import io.github.mudrichenkoevgeny.kmp.core.common.platform.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.model.ApiErrorResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonHttpHeaders
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun HttpClientConfig<*>.setupCommonConfig(baseUrl: String, deviceInfo: DeviceInfo) {
    install(ContentNegotiation) { json(FoundationJson) }

    install(Logging) {
        level = LogLevel.ALL
        logger = Logger.DEFAULT
    }

    install("DynamicHeaders") {
        requestPipeline.intercept(HttpRequestPipeline.State) {
            val traceId = Uuid.random().toHexDashString()
            context.header(CommonHttpHeaders.TRACE_HEADER_NAME, traceId)
        }
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
        handleResponseExceptionWithRequest { exception, _ ->
            val responseException = exception as? ResponseException
                ?: return@handleResponseExceptionWithRequest
            val response = responseException.response

            val apiErrorResponse = try {
                response.body<ApiErrorResponse>()
            } catch (e: Exception) {
                null
            }

            if (apiErrorResponse != null) {
                throw ApiException(apiErrorResponse)
            }
        }
    }
}