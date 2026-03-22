package io.github.mudrichenkoevgeny.kmp.core.common.testsupport

import kotlin.time.Clock
import kotlin.time.Instant

/**
 * [Clock] with a mutable epoch for deterministic tests and previews (not wired into production components).
 */
class MutableEpochTestClock(
    private var epochMilliseconds: Long
) : Clock {
    override fun now(): Instant = Instant.fromEpochMilliseconds(epochMilliseconds)

    fun advanceMilliseconds(delta: Long) {
        epochMilliseconds += delta
    }
}
