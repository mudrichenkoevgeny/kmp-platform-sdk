package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.Test

@InternalApi
@RunWith(ParameterizedRobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    sdk = [ROBOLECTRIC_SDK],
    application = Application::class,
    qualifiers = RobolectricDeviceQualifiers.Pixel5
)
class ListingChoiceDropdownScreenshotTest(
    private val stateName: String,
    private val selectedIds: Set<String>
) {

    @Test
    fun capture() = runComposeUiTest {
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
                MaterialTheme {
                    Surface {
                        ListingChoiceDropdown(
                            filter = filter,
                            selectedIds = selectedIds,
                            onSelectionChanged = {}
                        )
                    }
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@ListingChoiceDropdownScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("Selected", setOf(ListingChoiceDropdownTestData.OPTION_ACTIVE_ID)),
                arrayOf("Empty", emptySet<String>())
            )
        }
    }
}
