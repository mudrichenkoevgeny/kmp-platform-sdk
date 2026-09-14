package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing

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
class PagingFooterTest {

    @Test
    fun showsLoadingFooter_whenNextPageLoading() = runComposeUiTest {
        val state = PaginationState<Any>(
            items = listOf(object {}),
            isNextPageLoading = true
        )

        setContent {
            ComponentTestHarness {
                PagingFooter(state = state, onRetry = {})
            }
        }
    }
}
