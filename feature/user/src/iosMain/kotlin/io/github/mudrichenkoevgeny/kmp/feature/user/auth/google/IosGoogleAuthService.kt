package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIViewController
import kotlin.coroutines.resume

/**
 * iOS [GoogleAuthService] using Google Sign-In (`GIDSignIn`): configures `serverClientID` from [webClientId], presents from
 * [getRootController], and returns the Google ID token string on success.
 *
 * The current implementation resumes the sign-in coroutine immediately with a not-implemented failure, so the SDK callback
 * path below is effectively dead until that line is removed.
 *
 * @param getRootController Host callback that returns the presenting `UIViewController`.
 * @param webClientId Backend OAuth web client ID used as `serverClientID` when building `GIDConfiguration`.
 */
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