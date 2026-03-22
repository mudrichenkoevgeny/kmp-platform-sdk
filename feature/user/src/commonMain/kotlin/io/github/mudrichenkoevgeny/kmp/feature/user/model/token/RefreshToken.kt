package io.github.mudrichenkoevgeny.kmp.feature.user.model.token

import kotlin.jvm.JvmInline

/** Opaque refresh-token string from OAuth/session responses; not parsed beyond wrapping. */
@JvmInline
value class RefreshToken(val value: String)