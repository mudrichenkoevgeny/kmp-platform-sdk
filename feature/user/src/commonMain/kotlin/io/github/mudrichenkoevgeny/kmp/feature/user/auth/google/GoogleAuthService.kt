package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult

interface GoogleAuthService {
    suspend fun signIn(): AppResult<String>
    suspend fun signOut(): AppResult<Unit>
}