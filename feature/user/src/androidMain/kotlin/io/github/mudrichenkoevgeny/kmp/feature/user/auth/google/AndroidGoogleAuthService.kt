package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android implementation of [GoogleAuthService] using the **Jetpack Credential Manager** API.
 *
 * This service handles Google Sign-In by requesting a Google ID Token from the system's
 * account picker and provides sign-out capabilities by clearing the credential state.
 *
 * @property context Android `Context` required to initialize [CredentialManager].
 * @property webClientId The server's OAuth 2.0 client ID (Web application type)
 * used to identify the app to Google's identity servers.
 */
public class AndroidGoogleAuthService(
    private val context: Context,
    private val webClientId: String
) : GoogleAuthService {

    private val credentialManager: CredentialManager = CredentialManager.create(context)

    public override suspend fun signIn(): AppResult<LoginByExternalAuthProviderRequest> = withContext(
        Dispatchers.Main
    ) {
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential

            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                AppResult.Success(
                    LoginByExternalAuthProviderRequest(
                        authProvider = UserAuthProvider.GOOGLE.value,
                        externalProviderToken = googleIdTokenCredential.idToken,
                        displayName = googleIdTokenCredential.displayName
                    )
                )
            } else {
                AppResult.Error(
                    UserError.ExternalAuthFailed(
                        throwable = IllegalStateException("Received unexpected credential type: ${credential.type}")
                    )
                )
            }
        } catch (e: GetCredentialCancellationException) {
            AppResult.Error(UserError.ExternalAuthCancelled(throwable = e))
        } catch (e: GetCredentialException) {
            AppResult.Error(UserError.ExternalAuthFailed(throwable = e))
        } catch (e: GoogleIdTokenParsingException) {
            AppResult.Error(UserError.ExternalAuthFailed(throwable = e))
        } catch (e: IllegalArgumentException) {
            AppResult.Error(UserError.ExternalAuthFailed(throwable = e))
        }
    }

    public override suspend fun signOut(): AppResult<Unit> = withContext(Dispatchers.Main) {
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            AppResult.Success(Unit)
        } catch (e: ClearCredentialException) {
            AppResult.Error(UserError.ExternalAuthFailed(throwable = e))
        }
    }
}
