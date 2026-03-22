package io.github.mudrichenkoevgeny.kmp.feature.user.ui.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

/**
 * Runs a coroutine test with [Dispatchers.Main] set to a [StandardTestDispatcher] that shares the
 * same scheduler as [runTest], so `advanceUntilIdle` drives work scheduled on the main dispatcher
 * by Decompose components (see `componentCoroutineScope`).
 */
fun runUserUiComponentTest(
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
 * For non-suspending tests that still construct Decompose components using [Dispatchers.Main]
 * (e.g. navigation stack child factories).
 */
fun withUserUiMainDispatcher(block: () -> Unit) {
    val dispatcher = UnconfinedTestDispatcher()
    Dispatchers.setMain(dispatcher)
    try {
        block()
    } finally {
        Dispatchers.resetMain()
    }
}
