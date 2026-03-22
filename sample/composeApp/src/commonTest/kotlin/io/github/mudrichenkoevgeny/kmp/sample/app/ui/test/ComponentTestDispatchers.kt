package io.github.mudrichenkoevgeny.kmp.sample.app.ui.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

/**
 * Runs a coroutine test with [Dispatchers.Main] aligned to the test scheduler so
 * [kotlinx.coroutines.test.advanceUntilIdle] drives work scheduled on the main dispatcher.
 */
fun runSampleComponentTest(
    testBody: suspend TestScope.() -> Unit
) = runTest {
    val mainDispatcher = StandardTestDispatcher(testScheduler)
    Dispatchers.setMain(mainDispatcher)
    try {
        testBody()
    } finally {
        Dispatchers.resetMain()
    }
}

/**
 * For synchronous blocks that construct Decompose components relying on [Dispatchers.Main].
 */
fun withSampleUiMainDispatcher(block: () -> Unit) {
    val dispatcher = UnconfinedTestDispatcher()
    Dispatchers.setMain(dispatcher)
    try {
        block()
    } finally {
        Dispatchers.resetMain()
    }
}
