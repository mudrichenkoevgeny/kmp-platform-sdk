package io.github.mudrichenkoevgeny.kmp.feature.user.model.token

import kotlin.jvm.JvmInline

/** Opaque access-token string from OAuth/session responses; not parsed beyond wrapping. */
@JvmInline
value class AccessToken(val value: String)