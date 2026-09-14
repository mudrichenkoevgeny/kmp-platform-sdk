package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    sdk = [ROBOLECTRIC_SDK],
    application = Application::class,
    qualifiers = RobolectricDeviceQualifiers.Pixel5
)
class ListingOptionsPanelScreenshotTest {

    @Test
    fun capture() = runComposeUiTest {
        val config = ListingOptionsConfig(
            sortOptions = listOf(
                ListingSortDefinition(
                    ListingOptionsPanelTestData.SORT_ID_CREATED_AT,
                    ListingOptionsPanelTestData.SORT_TITLE_CREATED_AT
                )
            ),
            filters = listOf(
                TextListingFilterDefinition(
                    ListingOptionsPanelTestData.FILTER_ID_ACTION,
                    ListingOptionsPanelTestData.FILTER_TITLE_ACTION,
                    ListingOptionsPanelTestData.FILTER_PLACEHOLDER_SEARCH
                )
            )
        )

        setContent {
            ComponentTestHarness {
                MaterialTheme {
                    Surface {
                        ListingOptionsPanel(
                            config = config,
                            sortState = ListingSortState(ListingOptionsPanelTestData.SORT_ID_CREATED_AT, false),
                            filterStates = emptyMap(),
                            onSortChanged = {},
                            onFilterChanged = { _, _ -> },
                            onApplyClick = {}
                        )
                    }
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@ListingOptionsPanelScreenshotTest,
            stateName = "Default"
        )
    }
}
