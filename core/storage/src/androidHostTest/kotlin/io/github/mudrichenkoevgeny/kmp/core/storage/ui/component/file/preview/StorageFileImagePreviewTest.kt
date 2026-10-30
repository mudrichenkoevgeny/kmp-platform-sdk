package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class StorageFileImagePreviewTest {

    @Test
    fun displaysImagePreviewContainer() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                StorageFileImagePreview(
                    imageUrl = "https://example.com/test.png",
                    category = StorageCategory.IMAGE
                )
            }
        }

        onNodeWithTag(StorageFileImagePreviewTestTags.CONTAINER).assertIsDisplayed()
    }
}
