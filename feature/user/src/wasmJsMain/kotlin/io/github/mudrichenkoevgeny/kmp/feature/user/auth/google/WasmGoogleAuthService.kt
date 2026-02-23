package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class WasmGoogleAuthService(
    private val webClientId: String
) : GoogleAuthService {

    @OptIn(ExperimentalWasmJsInterop::class)
    override suspend fun signIn(): AppResult<String> = suspendCancellableCoroutine { continuation ->
        try {
            google.accounts.id.initialize(
                createGoogleConfig(webClientId) { response ->
                    val token = getCredentialFromJsResponse(response)
                    if (continuation.isActive) {
                        if (token.isNotEmpty()) {
                            continuation.resume(AppResult.Success(token))
                        } else {
                            continuation.resume(
                                AppResult.Error(UserError.ExternalAuthFailed(
                                    Exception("Google Auth: Token is empty")
                                ))
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
                                    Exception("Google Prompt was dismissed or skipped")
                                )
                            )
                        )
                    }
                }
            }

        } catch (e: Exception) {
            if (continuation.isActive) {
                continuation.resume(AppResult.Error(UserError.ExternalAuthFailed(e)))
            }
        }
    }

    override suspend fun signOut(): AppResult<Unit> {
        return AppResult.Success(Unit)
    }
}