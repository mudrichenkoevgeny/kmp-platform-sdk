package io.github.mudrichenkoevgeny.kmp.core.common.testsupport

import kotlin.time.Clock
import kotlin.time.Instant

/**
 * [Clock] with a mutable epoch for deterministic tests and previews (not wired into production components).
 */
public class MutableEpochTestClock(
    private var epochMilliseconds: Long
) : Clock {
    public override fun now(): Instant = Instant.fromEpochMilliseconds(epochMilliseconds)

    public fun advanceMilliseconds(delta: Long) {
        epochMilliseconds += delta
    }
}
