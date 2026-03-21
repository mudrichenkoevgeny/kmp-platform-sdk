package io.github.mudrichenkoevgeny.kmp.core.common.storage

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class AndroidSettingsFactoryTest {

    @Test
    fun `create fails when platformContext is null`() {
        assertFailsWith<NullPointerException> {
            getSettingsFactory(null).create()
        }
    }

    @Test
    fun `create fails when platformContext is not Context`() {
        assertFailsWith<ClassCastException> {
            getSettingsFactory("not-context").create()
        }
    }

    @Test
    fun `create returns EncryptedSettings that round trips preferences`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val key = "android_settings_factory_test_${Random.nextInt()}"

        val storage = getSettingsFactory(context).create()

        assertNull(storage.get(key))

        storage.put(key, "stored-value")
        assertEquals("stored-value", storage.get(key))

        storage.remove(key)
        assertNull(storage.get(key))
    }
}
