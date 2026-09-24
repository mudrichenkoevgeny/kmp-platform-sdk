package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class ListingHeaderBarTest {

    @Test
    fun emptyState_rendersNothing() = runComposeUiTest {
        val state = PaginationState<String>(items = emptyList())

        setContent {
            ComponentTestHarness {
                ListingHeaderBar(
                    state = state,
                    lazyListState = rememberLazyListState(),
                    modifier = Modifier.testTag(TEST_TAG)
                )
            }
        }

        onNodeWithTag(TEST_TAG).assertDoesNotExist()
    }

    @Test
    fun contentState_displaysHeaderBar() = runComposeUiTest {
        val state = PaginationState(
            items = listOf("item1", "item2"),
            pageNumber = 1,
            totalPages = 5,
            totalCount = 100
        )

        setContent {
            ComponentTestHarness {
                ListingHeaderBar(
                    state = state,
                    lazyListState = rememberLazyListState(),
                    modifier = Modifier.testTag(TEST_TAG)
                )
            }
        }

        onNodeWithTag(TEST_TAG).assertIsDisplayed()
    }

    private companion object {
        const val TEST_TAG = "ListingHeaderBar"
    }
}
