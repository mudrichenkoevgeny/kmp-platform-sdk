package io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid

@JvmInline
@Serializable
value class UserIdentifierId(val value: Uuid) {
    fun asHexDashString(): String = value.toHexDashString()

    companion object {
        fun generate() = UserIdentifierId(Uuid.random())
    }
}

fun String.toUserIdentifierIdOrThrow(): UserIdentifierId =
    UserIdentifierId(Uuid.parse(this))