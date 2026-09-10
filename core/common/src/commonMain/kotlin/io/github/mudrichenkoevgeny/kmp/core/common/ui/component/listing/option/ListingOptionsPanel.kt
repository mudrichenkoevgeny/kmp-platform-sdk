package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.*
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
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
        androidx.compose.material3.Button(
            onClick = onApplyClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.ui_common_apply))
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
        horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium),
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
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = filter.title,
            style = MaterialTheme.typography.titleMedium
        )
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
                OutlinedTextField(
                    value = numberState?.value?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsed = newValue.toLongOrNull()
                        if (parsed == null) {
                            onFilterChanged(null)
                        } else {
                            onFilterChanged(NumberListingFilterState(parsed))
                        }
                    },
                    placeholder = { Text(text = filter.placeholder) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
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
                    horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
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
        horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
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

@Preview(showBackground = true)
@Composable
private fun ListingOptionsPanelPreview() {
    MaterialTheme {
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
