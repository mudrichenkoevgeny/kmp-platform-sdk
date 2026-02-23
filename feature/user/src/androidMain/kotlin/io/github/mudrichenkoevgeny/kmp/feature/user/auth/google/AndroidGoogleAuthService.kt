package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidGoogleAuthService(
    private val context: Context,
    private val webClientId: String
) : GoogleAuthService {

    private val credentialManager = CredentialManager.create(context)

    override suspend fun signIn(): AppResult<String> = withContext(Dispatchers.Main) {
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
                AppResult.Success(googleIdTokenCredential.idToken)
            } else {
                AppResult.Error(
                    UserError.ExternalAuthFailed(
                        throwable = Exception("Received unexpected credential type: ${credential.type}")
                    )
                )
            }
        } catch (e: GetCredentialCancellationException) {
            AppResult.Error(UserError.ExternalAuthCancelled(throwable = e))
        } catch (e: GetCredentialException) {
            AppResult.Error(UserError.ExternalAuthFailed(throwable = e))
        } catch (e: Exception) {
            AppResult.Error(UserError.ExternalAuthFailed(throwable = e))
        }
    }

    override suspend fun signOut(): AppResult<Unit> = withContext(Dispatchers.Main) {
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(UserError.ExternalAuthFailed(throwable = e))
        }
    }
}