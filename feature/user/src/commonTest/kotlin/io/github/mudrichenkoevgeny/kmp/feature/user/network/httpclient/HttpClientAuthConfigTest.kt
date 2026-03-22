package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient

import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.MockAuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Clock

private const val API_ORIGIN = "https://api.example.com"
private const val RESOURCE_PATH = "/resource"

private const val ACCESS_TOKEN_VALUE = "access-token"
private const val REFRESH_TOKEN_VALUE = "refresh-token"
private const val VALID_EXPIRY_OFFSET_MS = 3_600_000L
private const val EXPIRED_EXPIRES_AT_MS = 0L

private object SilentLogger : Logger {
    override fun log(message: String) {}
}

class HttpClientAuthConfigTest {

    @Test
    fun `protected request sends bearer when tokens are valid`() = runTest {
        val storage = MockAuthStorage()
        val expiresAt = Clock.System.now().toEpochMilliseconds() + VALID_EXPIRY_OFFSET_MS
        storage.updateTokens(
            AccessToken(ACCESS_TOKEN_VALUE),
            RefreshToken(REFRESH_TOKEN_VALUE),
            expiresAt
        )

        val authHeader = captureAuthHeader(storage) { http ->
            http.get("$API_ORIGIN$RESOURCE_PATH")
        }

        assertEquals("Bearer $ACCESS_TOKEN_VALUE", authHeader)
    }

    @Test
    fun `public request omits bearer when markAsPublic is used`() = runTest {
        val storage = MockAuthStorage()
        val expiresAt = Clock.System.now().toEpochMilliseconds() + VALID_EXPIRY_OFFSET_MS
        storage.updateTokens(
            AccessToken(ACCESS_TOKEN_VALUE),
            RefreshToken(REFRESH_TOKEN_VALUE),
            expiresAt
        )

        val authHeader = captureAuthHeader(storage) { http ->
            http.get("$API_ORIGIN$RESOURCE_PATH") {
                markAsPublic()
            }
        }

        assertNull(authHeader)
    }

    @Test
    fun `protected request omits bearer when access token is expired`() = runTest {
        val storage = MockAuthStorage()
        storage.updateTokens(
            AccessToken(ACCESS_TOKEN_VALUE),
            RefreshToken(REFRESH_TOKEN_VALUE),
            expiresAt = EXPIRED_EXPIRES_AT_MS
        )

        val authHeader = captureAuthHeader(storage) { http ->
            http.get("$API_ORIGIN$RESOURCE_PATH")
        }

        assertNull(authHeader)
    }

    private suspend fun captureAuthHeader(
        storage: MockAuthStorage,
        performRequest: suspend (HttpClient) -> Unit
    ): String? {
        var captured: String? = null
        val engine = MockEngine { req ->
            captured = req.headers[HttpHeaders.Authorization]
            respond(
                content = "{}",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HttpClient(engine) {
            setupAuthConfig(
                baseUrl = API_ORIGIN,
                networkLogger = SilentLogger,
                authStorage = storage
            )
        }
        try {
            performRequest(client)
        } finally {
            client.close()
        }
        return captured
    }
}
