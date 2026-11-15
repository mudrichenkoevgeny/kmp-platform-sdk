package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Web (Wasm) implementation of [GoogleAuthService] using the **Google Identity Services** (GSI) JavaScript library.
 *
 * This service leverages WebAssembly Interop to trigger the Google "One Tap" or standard
 * Sign-In prompt in a web browser.
 *
 * @property webClientId OAuth 2.0 client ID (Web application type) used for browser-based authentication.
 */
public class WasmGoogleAuthService(
    private val webClientId: String
) : GoogleAuthService {

    @OptIn(ExperimentalWasmJsInterop::class)
    public override suspend fun signIn(): AppResult<LoginByExternalAuthProviderRequest> =
        suspendCancellableCoroutine { continuation ->
            try {
                google.accounts.id.initialize(
                    createGoogleConfig(webClientId) { response ->
                        val token = getCredentialFromJsResponse(response)
                        if (continuation.isActive) {
                            if (token.isNotEmpty()) {
                                val displayName = getDisplayNameFromJsResponse(response)
                                continuation.resume(
                                    AppResult.Success(
                                        LoginByExternalAuthProviderRequest(
                                            authProvider = UserAuthProvider.GOOGLE.value,
                                            externalProviderToken = token,
                                            displayName = displayName
                                        )
                                    )
                                )
                            } else {
                                continuation.resume(
                                    AppResult.Error(
                                        UserError.ExternalAuthFailed(
                                            IllegalStateException("Google Auth: Token is empty")
                                        )
                                    )
                                )
                            }
                        }
                    }
                )

                google.accounts.id.prompt { notification: PromptMomentNotification ->
                    if (notification.isNotDisplayed() || notification.isSkippedMoment()) {
                        if (continuation.isActive) {
                            continuation.resume(
                                AppResult.Error(
                                    UserError.ExternalAuthCancelled(
                                        IllegalStateException("Google Prompt was dismissed or skipped")
                                    )
                                )
                            )
                        }
                    }
                }
            } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
                if (continuation.isActive) {
                    continuation.resume(AppResult.Error(UserError.ExternalAuthFailed(e)))
                }
            }
        }

    public override suspend fun signOut(): AppResult<Unit> = AppResult.Success(Unit)
}
