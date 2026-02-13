package io.github.mudrichenkoevgeny.kmp.core.common.error.model

import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@JvmInline
value class ErrorId(val value: Uuid) {
    fun asHexDashString(): String = value.toHexDashString()

    companion object {
        fun generate() = ErrorId(Uuid.random())
    }
}

@OptIn(ExperimentalUuidApi::class)
fun String.toErrorIdOrNull(): ErrorId? =
    Uuid.parseOrNull(this)?.let { ErrorId(it) }