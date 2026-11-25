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

    /**
     * Advances the mock epoch time by the specified delta in milliseconds.
     *
     * @param delta Milliseconds to advance.
     */
    public fun advanceMilliseconds(delta: Long) {
        epochMilliseconds += delta
    }
}
