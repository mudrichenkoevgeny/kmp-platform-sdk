package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIViewController
import kotlin.coroutines.resume

class IosGoogleAuthService(
    private val getRootController: () -> UIViewController,
    private val webClientId: String
) : GoogleAuthService {

    override suspend fun signIn(): Result<String> = suspendCancellableCoroutine { continuation ->
        continuation.resume(Result.failure(Exception("Not implemented")))
        val signIn = GIDSignIn.sharedInstance

        val config = GIDConfiguration(
            clientID = signIn.configuration?.clientID ?: "",
            serverClientID = webClientId
        )
        signIn.setConfiguration(config)

        signIn.signInWithPresentingViewController(getRootController()) { result, error ->
            if (error != null) {
                continuation.resume(Result.failure(Exception(error.localizedDescription)))
                return@signInWithPresentingViewController
            }

            val idToken = result?.user?.idToken?.tokenString

            if (idToken != null) {
                continuation.resume(Result.success(idToken))
            } else {
                continuation.resume(Result.failure(Exception("ID Token is missing")))
            }
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return runCatching {
            GIDSignIn.sharedInstance.signOut()
        }
    }
}