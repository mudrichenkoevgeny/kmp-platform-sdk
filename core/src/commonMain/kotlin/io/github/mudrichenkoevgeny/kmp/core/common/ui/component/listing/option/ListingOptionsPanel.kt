package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.NumberListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.NumberListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

/**
 * Panel component presenting sorting and filtering options for listings.
 *
 * @param config Configuration specifying available sort definitions and filter definitions.
 * @param sortState Current sorting state, or null if unsorted.
 * @param filterStates Map of filter identifiers to their current states.
 * @param onSortChange Callback invoked when the sort selection changes.
 * @param onFilterChange Callback invoked when a filter state changes.
 * @param onApplyClick Callback invoked when the apply button is pressed.
 * @param modifier Modifier to be applied to the panel layout.
 */
@Composable
public fun ListingOptionsPanel(
    config: ListingOptionsConfig,
    sortState: ListingSortState?,
    filterStates: Map<String, ListingFilterState>,
    onSortChange: (ListingSortState) -> Unit,
    onFilterChange: (String, ListingFilterState?) -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 480.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = CoreTheme.dimens.elevationHeader
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(CoreTheme.dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
        ) {
            if (config.sortOptions.isNotEmpty()) {
                ListingSortSection(
                    options = config.sortOptions,
                    currentState = sortState,
                    onSortChange = onSortChange
                )
            }
            if (config.filters.isNotEmpty()) {
                ListingFiltersSection(
                    filters = config.filters,
                    states = filterStates,
                    onFilterChange = onFilterChange
                )
            }
            Button(
                onClick = onApplyClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(LocalCommonStrings.current.ui.apply)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListingSortSection(
    options: List<ListingSortDefinition>,
    currentState: ListingSortState?,
    onSortChange: (ListingSortState) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val selectedOption = options.find { it.id == currentState?.optionId } ?: options.first()
    val isAscending = currentState?.isAscending ?: false

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (options.size == 1) {
            Text(
                text = selectedOption.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
        } else {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedOption.title,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option.title) },
                            onClick = {
                                onSortChange(
                                    ListingSortState(
                                        optionId = option.id,
                                        isAscending = isAscending
                                    )
                                )
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
        FilterChip(
            selected = isAscending,
            onClick = {
                onSortChange(
                    ListingSortState(
                        optionId = selectedOption.id,
                        isAscending = true
                    )
                )
            },
            label = { Text(text = LocalCommonStrings.current.ui.sortAsc) }
        )
        FilterChip(
            selected = !isAscending,
            onClick = {
                onSortChange(
                    ListingSortState(
                        optionId = selectedOption.id,
                        isAscending = false
                    )
                )
            },
            label = { Text(text = LocalCommonStrings.current.ui.sortDesc) }
        )
    }
}

@Composable
private fun ListingFiltersSection(
    filters: List<ListingFilterDefinition>,
    states: Map<String, ListingFilterState>,
    onFilterChange: (String, ListingFilterState?) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium),
        modifier = Modifier.fillMaxWidth()
    ) {
        filters.forEach { filter ->
            ListingFilterItem(
                filter = filter,
                state = states[filter.id],
                onFilterChange = { newState ->
                    onFilterChange(filter.id, newState)
                }
            )
        }
    }
}

@Composable
private fun ListingFilterItem(
    filter: ListingFilterDefinition,
    state: ListingFilterState?,
    onFilterChange: (ListingFilterState?) -> Unit
) {
    val isFilterActive = when (filter) {
        is NumberListingFilterDefinition -> {
            val numberState = state as? NumberListingFilterState
            numberState != null && numberState.value != filter.defaultValueOnFocusLost
        }

        else -> state != null
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = filter.title,
                style = MaterialTheme.typography.titleMedium
            )
            if (isFilterActive) {
                TextButton(
                    onClick = { onFilterChange(null) }
                ) {
                    Text(
                        text = LocalCommonStrings.current.ui.clearAll,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        when (filter) {
            is TextListingFilterDefinition -> TextFilterItem(
                filter = filter,
                state = state as? TextListingFilterState,
                onFilterChange = onFilterChange
            )

            is NumberListingFilterDefinition -> NumberFilterItem(
                filter = filter,
                state = state as? NumberListingFilterState,
                onFilterChange = onFilterChange
            )

            is BooleanListingFilterDefinition -> BooleanFilterItem(
                filter = filter,
                state = state as? BooleanListingFilterState,
                onFilterChange = onFilterChange
            )

            is ChoiceListingFilterDefinition -> ChoiceFilterItem(
                filter = filter,
                state = state as? ChoiceListingFilterState,
                onFilterChange = onFilterChange
            )
        }
    }
}

@Composable
private fun TextFilterItem(
    filter: TextListingFilterDefinition,
    state: TextListingFilterState?,
    onFilterChange: (ListingFilterState?) -> Unit
) {
    OutlinedTextField(
        value = state?.value ?: "",
        onValueChange = { newValue ->
            if (newValue.isEmpty()) {
                onFilterChange(null)
            } else {
                onFilterChange(TextListingFilterState(newValue))
            }
        },
        placeholder = { Text(text = filter.placeholder) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun NumberFilterItem(
    filter: NumberListingFilterDefinition,
    state: NumberListingFilterState?,
    onFilterChange: (ListingFilterState?) -> Unit
) {
    var rawText by remember {
        mutableStateOf(state?.value?.toString() ?: "")
    }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (!isFocused) {
            rawText = state?.value?.toString() ?: ""
        }
    }

    OutlinedTextField(
        value = rawText,
        onValueChange = { newValue ->
            if (newValue.isEmpty()) {
                rawText = ""
                onFilterChange(null)
            } else {
                val parsed = newValue.toLongOrNull()
                if (parsed != null) {
                    val clamped = if (filter.minValue != null && filter.maxValue != null) {
                        parsed.coerceIn(filter.minValue, filter.maxValue)
                    } else if (filter.minValue != null) {
                        parsed.coerceAtLeast(filter.minValue)
                    } else if (filter.maxValue != null) {
                        parsed.coerceAtMost(filter.maxValue)
                    } else {
                        parsed
                    }
                    rawText = clamped.toString()
                    onFilterChange(NumberListingFilterState(clamped))
                }
            }
        },
        placeholder = { Text(text = filter.placeholder) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (!focusState.isFocused && rawText.isEmpty() && filter.defaultValueOnFocusLost != null) {
                    rawText = filter.defaultValueOnFocusLost.toString()
                    onFilterChange(NumberListingFilterState(filter.defaultValueOnFocusLost))
                }
            }
    )
}

@Composable
private fun BooleanFilterItem(
    filter: BooleanListingFilterDefinition,
    state: BooleanListingFilterState?,
    onFilterChange: (ListingFilterState?) -> Unit
) {
    val trueLabel = filter.trueLabel ?: LocalCommonStrings.current.ui.yes
    val falseLabel = filter.falseLabel ?: LocalCommonStrings.current.ui.no
    Row(
        horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
    ) {
        FilterChip(
            selected = state?.value == true,
            onClick = {
                if (state?.value == true) {
                    onFilterChange(null)
                } else {
                    onFilterChange(BooleanListingFilterState(true))
                }
            },
            label = { Text(text = trueLabel) }
        )
        FilterChip(
            selected = state?.value == false,
            onClick = {
                if (state?.value == false) {
                    onFilterChange(null)
                } else {
                    onFilterChange(BooleanListingFilterState(false))
                }
            },
            label = { Text(text = falseLabel) }
        )
    }
}

@Composable
private fun ChoiceFilterItem(
    filter: ChoiceListingFilterDefinition,
    state: ChoiceListingFilterState?,
    onFilterChange: (ListingFilterState?) -> Unit
) {
    val selectedIds = state?.selectedIds ?: emptySet()
    val effectiveStyle = when (filter.presentationStyle) {
        ChoiceFilterPresentationStyle.AUTO -> if (filter.options.size <= 4) {
            ChoiceFilterPresentationStyle.CHIPS
        } else {
            ChoiceFilterPresentationStyle.DROPDOWN
        }

        else -> filter.presentationStyle
    }

    when (effectiveStyle) {
        ChoiceFilterPresentationStyle.CHIPS -> {
            ListingChoiceChips(
                filter = filter,
                selectedIds = selectedIds,
                onSelectionChange = { newSelection ->
                    if (newSelection.isEmpty()) {
                        onFilterChange(null)
                    } else {
                        onFilterChange(ChoiceListingFilterState(newSelection))
                    }
                }
            )
        }

        else -> {
            ListingChoiceDropdown(
                filter = filter,
                selectedIds = selectedIds,
                onSelectionChange = { newSelection ->
                    if (newSelection.isEmpty()) {
                        onFilterChange(null)
                    } else {
                        onFilterChange(ChoiceListingFilterState(newSelection))
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ListingChoiceChips(
    filter: ChoiceListingFilterDefinition,
    selectedIds: Set<String>,
    onSelectionChange: (Set<String>) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall),
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall),
        modifier = Modifier.fillMaxWidth()
    ) {
        filter.options.forEach { option ->
            val isSelected = selectedIds.contains(option.id)
            FilterChip(
                selected = isSelected,
                onClick = {
                    val newSelection = if (isSelected) {
                        selectedIds - option.id
                    } else {
                        if (filter.isMultiple) {
                            selectedIds + option.id
                        } else {
                            setOf(option.id)
                        }
                    }
                    onSelectionChange(newSelection)
                },
                label = { Text(text = option.title) }
            )
        }
    }
}

@InternalApi
@Composable
private fun ListingOptionsPanelPreviewContent() {
    ListingOptionsPanel(
        config = ListingOptionsConfig(
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
        ),
        sortState = ListingSortState(ListingOptionsPanelTestData.SORT_ID_CREATED_AT, false),
        filterStates = emptyMap(),
        onSortChange = {},
        onFilterChange = { _, _ -> },
        onApplyClick = {}
    )
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun ListingOptionsPanelComponentSizePreview() {
    ComponentPreviewContainer {
        ListingOptionsPanelPreviewContent()
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ListingOptionsPanelThemePreview() {
    ComponentPreviewContainer {
        ListingOptionsPanelPreviewContent()
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun ListingOptionsPanelFontScalePreview() {
    ComponentPreviewContainer {
        ListingOptionsPanelPreviewContent()
    }
}

internal object ListingOptionsPanelTestData {
    const val SORT_ID_CREATED_AT = "created_at"
    const val SORT_TITLE_CREATED_AT = "Created Date"
    const val FILTER_ID_ACTION = "action"
    const val FILTER_TITLE_ACTION = "Action"
    const val FILTER_PLACEHOLDER_SEARCH = "Search..."
    const val BUTTON_APPLY_LABEL = "Apply"
}
