package io.github.mudrichenkoevgeny.kmp.feature.user.model.user

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class UserId(val value: Uuid) {
    fun asHexDashString(): String = value.toHexDashString()

    companion object {
        fun generate() = UserId(Uuid.random())
    }
}

fun String.toUserIdOrNull(): UserId? =
    Uuid.parseOrNull(this)?.let { UserId(it) }

fun String.toUserIdOrThrow(): UserId = UserId(Uuid.parse(this))