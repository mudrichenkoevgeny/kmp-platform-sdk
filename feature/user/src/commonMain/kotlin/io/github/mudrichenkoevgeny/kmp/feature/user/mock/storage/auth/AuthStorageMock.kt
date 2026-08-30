package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Instant

/**
 * In-memory [AuthStorage] for tests and previews. Token fields and [accessTokenFlow] stay in sync, mirroring
 * `EncryptedAuthStorage` behavior without persistence.
 */
@InternalApi
class AuthStorageMock : AuthStorage {

    private var accessToken: AccessToken? = null
    private var refreshToken: RefreshToken? = null
    private var expiresAt: Long = 0L
    private var publicAuthSettings: PublicAuthSettings? = null
    private var managementAuthSettings: ManagementAuthSettings? = null

    private val _accessTokenFlow = MutableStateFlow<String?>(null)
    override val accessTokenFlow: StateFlow<String?> = _accessTokenFlow.asStateFlow()

    override suspend fun getAccessToken(): AccessToken? = accessToken

    override suspend fun getRefreshToken(): RefreshToken? = refreshToken

    override suspend fun getExpiresAt(): Long = expiresAt

    override suspend fun updateTokens(accessToken: AccessToken, refreshToken: RefreshToken, expiresAt: Instant) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.expiresAt = expiresAt.toEpochMilliseconds()
        _accessTokenFlow.value = accessToken.value
    }

    override suspend fun clearTokens() {
        accessToken = null
        refreshToken = null
        expiresAt = 0L
        _accessTokenFlow.value = null
    }

    override suspend fun getPublicAuthSettings(): PublicAuthSettings? = publicAuthSettings

    override suspend fun updatePublicAuthSettings(publicAuthSettings: PublicAuthSettings) {
        this.publicAuthSettings = publicAuthSettings
    }

    override suspend fun clearPublicAuthSettings() {
        publicAuthSettings = null
    }

    override suspend fun getManagementAuthSettings(): ManagementAuthSettings? = managementAuthSettings

    override suspend fun updateManagementAuthSettings(managementAuthSettings: ManagementAuthSettings) {
        this.managementAuthSettings = managementAuthSettings
    }

    override suspend fun clearManagementAuthSettings() {
        managementAuthSettings = null
    }
}