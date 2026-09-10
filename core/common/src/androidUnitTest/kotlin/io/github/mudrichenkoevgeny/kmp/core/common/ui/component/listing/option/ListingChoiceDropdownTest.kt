package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class ListingChoiceDropdownTest {

    @Test
    fun displaysSelectedOptionTitle_whenOptionIsSelected() = runComposeUiTest {
        val filter = ChoiceListingFilterDefinition(
            id = ListingChoiceDropdownTestData.FILTER_ID_STATUS,
            title = ListingChoiceDropdownTestData.FILTER_TITLE_STATUS,
            options = listOf(
                ListingFilterChoiceOption(
                    ListingChoiceDropdownTestData.OPTION_ACTIVE_ID,
                    ListingChoiceDropdownTestData.OPTION_ACTIVE_TITLE
                ),
                ListingFilterChoiceOption(
                    ListingChoiceDropdownTestData.OPTION_INACTIVE_ID,
                    ListingChoiceDropdownTestData.OPTION_INACTIVE_TITLE
                )
            )
        )

        setContent {
            ComponentTestHarness {
                ListingChoiceDropdown(
                    filter = filter,
                    selectedIds = setOf(ListingChoiceDropdownTestData.OPTION_ACTIVE_ID),
                    onSelectionChanged = {}
                )
            }
        }

        onNodeWithText(ListingChoiceDropdownTestData.OPTION_ACTIVE_TITLE).assertIsDisplayed()
    }

    @Test
    fun displaysAllLabel_whenNoOptionIsSelected() = runComposeUiTest {
        val filter = ChoiceListingFilterDefinition(
            id = ListingChoiceDropdownTestData.FILTER_ID_STATUS,
            title = ListingChoiceDropdownTestData.FILTER_TITLE_STATUS,
            options = listOf(
                ListingFilterChoiceOption(
                    ListingChoiceDropdownTestData.OPTION_ACTIVE_ID,
                    ListingChoiceDropdownTestData.OPTION_ACTIVE_TITLE
                ),
                ListingFilterChoiceOption(
                    ListingChoiceDropdownTestData.OPTION_INACTIVE_ID,
                    ListingChoiceDropdownTestData.OPTION_INACTIVE_TITLE
                )
            )
        )

        setContent {
            ComponentTestHarness {
                ListingChoiceDropdown(
                    filter = filter,
                    selectedIds = emptySet(),
                    onSelectionChanged = {}
                )
            }
        }

        onNodeWithText(ListingChoiceDropdownTestData.LABEL_ALL).assertIsDisplayed()
    }
}
