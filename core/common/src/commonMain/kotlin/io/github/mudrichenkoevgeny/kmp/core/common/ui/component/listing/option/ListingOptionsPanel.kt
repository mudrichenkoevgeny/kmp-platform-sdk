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
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.Surface
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
import io.github.mudrichenkoevgeny.kmp.core.common.Res
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_apply
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_clear_all
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_no
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_sort_asc
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_sort_desc
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_yes
import org.jetbrains.compose.resources.stringResource

@Composable
fun ListingOptionsPanel(
    config: ListingOptionsConfig,
    sortState: ListingSortState?,
    filterStates: Map<String, ListingFilterState>,
    onSortChanged: (ListingSortState) -> Unit,
    onFilterChanged: (String, ListingFilterState?) -> Unit,
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
                    onSortChanged = onSortChanged
                )
            }
            if (config.filters.isNotEmpty()) {
                ListingFiltersSection(
                    filters = config.filters,
                    states = filterStates,
                    onFilterChanged = onFilterChanged
                )
            }
            Button(
                onClick = onApplyClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.ui_common_apply))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListingSortSection(
    options: List<ListingSortDefinition>,
    currentState: ListingSortState?,
    onSortChanged: (ListingSortState) -> Unit
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
                                onSortChanged(
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
                onSortChanged(
                    ListingSortState(
                        optionId = selectedOption.id,
                        isAscending = true
                    )
                )
            },
            label = { Text(text = stringResource(Res.string.ui_common_sort_asc)) }
        )
        FilterChip(
            selected = !isAscending,
            onClick = {
                onSortChanged(
                    ListingSortState(
                        optionId = selectedOption.id,
                        isAscending = false
                    )
                )
            },
            label = { Text(text = stringResource(Res.string.ui_common_sort_desc)) }
        )
    }
}

@Composable
private fun ListingFiltersSection(
    filters: List<ListingFilterDefinition>,
    states: Map<String, ListingFilterState>,
    onFilterChanged: (String, ListingFilterState?) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium),
        modifier = Modifier.fillMaxWidth()
    ) {
        filters.forEach { filter ->
            ListingFilterItem(
                filter = filter,
                state = states[filter.id],
                onFilterChanged = { newState ->
                    onFilterChanged(filter.id, newState)
                }
            )
        }
    }
}

@Composable
private fun ListingFilterItem(
    filter: ListingFilterDefinition,
    state: ListingFilterState?,
    onFilterChanged: (ListingFilterState?) -> Unit
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
                    onClick = { onFilterChanged(null) }
                ) {
                    Text(
                        text = stringResource(Res.string.ui_common_clear_all),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        when (filter) {
            is TextListingFilterDefinition -> {
                val textState = state as? TextListingFilterState
                OutlinedTextField(
                    value = textState?.value ?: "",
                    onValueChange = { newValue ->
                        if (newValue.isEmpty()) {
                            onFilterChanged(null)
                        } else {
                            onFilterChanged(TextListingFilterState(newValue))
                        }
                    },
                    placeholder = { Text(text = filter.placeholder) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is NumberListingFilterDefinition -> {
                val numberState = state as? NumberListingFilterState
                var rawText by remember {
                    mutableStateOf(numberState?.value?.toString() ?: "")
                }
                var isFocused by remember { mutableStateOf(false) }

                LaunchedEffect(numberState) {
                    if (!isFocused) {
                        rawText = numberState?.value?.toString() ?: ""
                    }
                }

                OutlinedTextField(
                    value = rawText,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty()) {
                            rawText = ""
                            onFilterChanged(null)
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
                                onFilterChanged(NumberListingFilterState(clamped))
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
                                onFilterChanged(NumberListingFilterState(filter.defaultValueOnFocusLost))
                            }
                        }
                )
            }
            is BooleanListingFilterDefinition -> {
                val booleanState = state as? BooleanListingFilterState
                val trueLabel = if (filter.trueLabelRes != null) {
                    stringResource(filter.trueLabelRes)
                } else {
                    stringResource(Res.string.ui_common_yes)
                }
                val falseLabel = if (filter.falseLabelRes != null) {
                    stringResource(filter.falseLabelRes)
                } else {
                    stringResource(Res.string.ui_common_no)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
                ) {
                    FilterChip(
                        selected = booleanState?.value == true,
                        onClick = {
                            if (booleanState?.value == true) {
                                onFilterChanged(null)
                            } else {
                                onFilterChanged(BooleanListingFilterState(true))
                            }
                        },
                        label = { Text(text = trueLabel) }
                    )
                    FilterChip(
                        selected = booleanState?.value == false,
                        onClick = {
                            if (booleanState?.value == false) {
                                onFilterChanged(null)
                            } else {
                                onFilterChanged(BooleanListingFilterState(false))
                            }
                        },
                        label = { Text(text = falseLabel) }
                    )
                }
            }
            is ChoiceListingFilterDefinition -> {
                val choiceState = state as? ChoiceListingFilterState
                val selectedIds = choiceState?.selectedIds ?: emptySet()
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
                            onSelectionChanged = { newSelection ->
                                if (newSelection.isEmpty()) {
                                    onFilterChanged(null)
                                } else {
                                    onFilterChanged(ChoiceListingFilterState(newSelection))
                                }
                            }
                        )
                    }
                    else -> {
                        ListingChoiceDropdown(
                            filter = filter,
                            selectedIds = selectedIds,
                            onSelectionChanged = { newSelection ->
                                if (newSelection.isEmpty()) {
                                    onFilterChanged(null)
                                } else {
                                    onFilterChanged(ChoiceListingFilterState(newSelection))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ListingChoiceChips(
    filter: ChoiceListingFilterDefinition,
    selectedIds: Set<String>,
    onSelectionChanged: (Set<String>) -> Unit
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
                    onSelectionChanged(newSelection)
                },
                label = { Text(text = option.title) }
            )
        }
    }
}

@ComponentSizePreviews
@Composable
private fun ListingOptionsPanelComponentSizePreview() {
    CoreTheme {
        Surface {
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
                onSortChanged = {},
                onFilterChanged = { _, _ -> },
                onApplyClick = {}
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ListingOptionsPanelThemePreview() {
    CoreTheme {
        Surface {
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
                onSortChanged = {},
                onFilterChanged = { _, _ -> },
                onApplyClick = {}
            )
        }
    }
}

@FontScalePreviews
@Composable
private fun ListingOptionsPanelFontScalePreview() {
    CoreTheme {
        Surface {
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
                onSortChanged = {},
                onFilterChanged = { _, _ -> },
                onApplyClick = {}
            )
        }
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