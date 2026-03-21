package io.github.mudrichenkoevgeny.kmp.feature.user.model.session

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class UserSessionId(val value: Uuid) {
    fun asHexDashString(): String = value.toHexDashString()

    companion object {
        fun generate() = UserSessionId(Uuid.random())
    }
}

fun String.toUserSessionIdOrThrow(): UserSessionId =
    UserSessionId(Uuid.parse(this))