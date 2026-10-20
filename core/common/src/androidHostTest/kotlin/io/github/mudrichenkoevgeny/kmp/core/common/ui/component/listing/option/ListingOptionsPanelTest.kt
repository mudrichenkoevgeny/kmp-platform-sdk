package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class ListingOptionsPanelTest {

    @Test
    fun rendersSortAndFilterOptions_andInvokesApplyClick() = runComposeUiTest {
        var applyClicked = false
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
                ListingOptionsPanel(
                    config = config,
                    sortState = ListingSortState(ListingOptionsPanelTestData.SORT_ID_CREATED_AT, false),
                    filterStates = emptyMap(),
                    onSortChanged = {},
                    onFilterChanged = { _, _ -> },
                    onApplyClick = { applyClicked = true }
                )
            }
        }

        onNodeWithText(ListingOptionsPanelTestData.FILTER_TITLE_ACTION).assertIsDisplayed()
        onNodeWithText(ListingOptionsPanelTestData.BUTTON_APPLY_LABEL).assertIsDisplayed()
        onNodeWithText(ListingOptionsPanelTestData.BUTTON_APPLY_LABEL).performClick()
        assertEquals(true, applyClicked)
    }
}
