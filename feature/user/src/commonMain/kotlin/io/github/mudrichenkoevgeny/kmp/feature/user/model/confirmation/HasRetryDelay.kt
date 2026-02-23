package io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation

interface HasRetryDelay {
    val retryAfterSeconds: Int
}