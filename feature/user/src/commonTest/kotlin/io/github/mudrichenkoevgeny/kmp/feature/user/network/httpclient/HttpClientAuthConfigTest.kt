package io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.model.ApiErrorResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.auth.refreshtoken.OpenRefreshTokenRoutes
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

private object SilentLogger : Logger {
    override fun log(message: String) {}
}

@InternalApi
class HttpClientAuthConfigTest {

    @Test
    fun `protected request sends bearer when tokens are valid`() = runTest {
        val storage = AuthStorageMock()
        val expiresAt = Instant.fromEpochMilliseconds(
            Clock.System.now().toEpochMilliseconds() + 1.hours.inWholeMilliseconds
        )

        val token = SessionToken(
            accessToken = AccessToken(ACCESS_TOKEN_VALUE),
            refreshToken = RefreshToken(REFRESH_TOKEN_VALUE),
            expiresAt = expiresAt,
            sessionId = UserSessionId.generate(),
            identifierId = UserIdentifierId.generate()
        )
        storage.updateTokens(token)

        val authHeader = captureAuthHeader(storage) { http ->
            http.get("$API_ORIGIN$RESOURCE_PATH")
        }

        assertEquals("Bearer $ACCESS_TOKEN_VALUE", authHeader)
    }

    @Test
    fun `public request omits bearer when markAsPublic is used`() = runTest {
        val storage = AuthStorageMock()
        val expiresAt = Instant.fromEpochMilliseconds(
            Clock.System.now().toEpochMilliseconds() + 1.hours.inWholeMilliseconds
        )

        val token = SessionToken(
            accessToken = AccessToken(ACCESS_TOKEN_VALUE),
            refreshToken = RefreshToken(REFRESH_TOKEN_VALUE),
            expiresAt = expiresAt,
            sessionId = UserSessionId.generate(),
            identifierId = UserIdentifierId.generate()
        )
        storage.updateTokens(token)

        val authHeader = captureAuthHeader(storage) { http ->
            http.get("$API_ORIGIN$RESOURCE_PATH") {
                markAsPublic()
            }
        }

        assertNull(authHeader)
    }

    @Test
    fun `protected request omits bearer when access token is expired`() = runTest {
        val storage = AuthStorageMock()
        val expiredAt = Instant.fromEpochMilliseconds(0)

        val expiredToken = SessionToken(
            accessToken = AccessToken(ACCESS_TOKEN_VALUE),
            refreshToken = RefreshToken(REFRESH_TOKEN_VALUE),
            expiresAt = expiredAt,
            sessionId = UserSessionId.generate(),
            identifierId = UserIdentifierId.generate()
        )
        storage.updateTokens(expiredToken)

        val authHeader = captureAuthHeader(storage) { http ->
            http.get("$API_ORIGIN$RESOURCE_PATH")
        }

        assertNull(authHeader)
    }

    @Test
    fun `session invalidating error code triggers onSessionCleared`() = runTest {
        var sessionClearedInvoked = false
        val storage = AuthStorageMock()
        val expiresAt = Instant.fromEpochMilliseconds(
            Clock.System.now().toEpochMilliseconds() + 1.hours.inWholeMilliseconds
        )

        val token = SessionToken(
            accessToken = AccessToken(ACCESS_TOKEN_VALUE),
            refreshToken = RefreshToken(REFRESH_TOKEN_VALUE),
            expiresAt = expiresAt,
            sessionId = UserSessionId.generate(),
            identifierId = UserIdentifierId.generate()
        )
        storage.updateTokens(token)

        val errorResponseJson = Json.encodeToString(
            ApiErrorResponse.serializer(),
            ApiErrorResponse(
                id = "123",
                code = UserErrorCodes.USER_LOCKED,
                message = "Locked",
                args = emptyMap()
            )
        )

        val engine = MockEngine {
            respond(
                content = errorResponseJson,
                status = HttpStatusCode.Forbidden,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            setupAuthConfig(
                baseUrl = API_ORIGIN,
                networkLogger = SilentLogger,
                authStorage = storage,
                refreshTokenRoute = OpenRefreshTokenRoutes.REFRESH_TOKEN,
                onSessionCleared = { sessionClearedInvoked = true }
            )
        }

        try {
            client.get("$API_ORIGIN$RESOURCE_PATH")
        } catch (_: Exception) {
        } finally {
            client.close()
        }

        assertTrue(sessionClearedInvoked)
    }

    @Test
    fun `non session invalidating error code does not trigger onSessionCleared`() = runTest {
        var sessionClearedInvoked = false
        val storage = AuthStorageMock()
        val expiresAt = Instant.fromEpochMilliseconds(
            Clock.System.now().toEpochMilliseconds() + 1.hours.inWholeMilliseconds
        )

        val token = SessionToken(
            accessToken = AccessToken(ACCESS_TOKEN_VALUE),
            refreshToken = RefreshToken(REFRESH_TOKEN_VALUE),
            expiresAt = expiresAt,
            sessionId = UserSessionId.generate(),
            identifierId = UserIdentifierId.generate()
        )
        storage.updateTokens(token)

        val errorResponseJson = Json.encodeToString(
            ApiErrorResponse.serializer(),
            ApiErrorResponse(
                id = "123",
                code = "NON_INVALIDATING_ERROR",
                message = "Non invalidating error",
                args = emptyMap()
            )
        )

        val engine = MockEngine {
            respond(
                content = errorResponseJson,
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            setupAuthConfig(
                baseUrl = API_ORIGIN,
                networkLogger = SilentLogger,
                authStorage = storage,
                refreshTokenRoute = OpenRefreshTokenRoutes.REFRESH_TOKEN,
                onSessionCleared = { sessionClearedInvoked = true }
            )
        }

        try {
            client.get("$API_ORIGIN$RESOURCE_PATH")
        } catch (_: Exception) {
        } finally {
            client.close()
        }

        assertFalse(sessionClearedInvoked)
    }

    private suspend fun captureAuthHeader(
        storage: AuthStorageMock,
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
                authStorage = storage,
                refreshTokenRoute = OpenRefreshTokenRoutes.REFRESH_TOKEN
            )
        }
        try {
            performRequest(client)
        } finally {
            client.close()
        }
        return captured
    }

    private companion object {
        const val API_ORIGIN = "https://api.example.com"
        const val RESOURCE_PATH = "/protected"
        const val ACCESS_TOKEN_VALUE = "access-123"
        const val REFRESH_TOKEN_VALUE = "refresh-123"
    }
}