package io.github.mudrichenkoevgeny.kmp.feature.user.model.auth

import kotlinx.serialization.Serializable

/** Supported unlock channel methods for OTP confirmation. */
@Serializable
enum class UnlockMethod {
    EMAIL,
    PHONE
}
