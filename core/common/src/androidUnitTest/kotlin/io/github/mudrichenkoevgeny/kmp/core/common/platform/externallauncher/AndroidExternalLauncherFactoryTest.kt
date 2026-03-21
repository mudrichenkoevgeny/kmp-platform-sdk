package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

import kotlin.test.Test
import kotlin.test.assertFailsWith

class AndroidExternalLauncherFactoryTest {

    @Test
    fun `getExternalLauncher fails when platformContext is null`() {
        assertFailsWith<IllegalStateException> {
            getExternalLauncher(null)
        }
    }

    @Test
    fun `getExternalLauncher fails when platformContext is not Context`() {
        assertFailsWith<IllegalStateException> {
            getExternalLauncher("not-a-context")
        }
    }
}
