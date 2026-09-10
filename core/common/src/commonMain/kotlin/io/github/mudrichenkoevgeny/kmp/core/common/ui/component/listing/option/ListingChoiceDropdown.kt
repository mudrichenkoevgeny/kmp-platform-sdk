package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingChoiceDropdown(
    filter: ChoiceListingFilterDefinition,
    selectedIds: Set<String>,
    onSelectionChanged: (Set<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val selectedOptions = filter.options.filter { selectedIds.contains(it.id) }
    val displayValue = when {
        selectedOptions.isEmpty() -> stringResource(Res.string.ui_common_all)
        selectedOptions.size <= 2 -> selectedOptions.joinToString(", ") { it.title }
        else -> stringResource(Res.string.ui_common_selected_count, selectedOptions.size)
    }

    val filteredOptions = if (searchQuery.isBlank()) {
        filter.options
    } else {
        filter.options.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = displayValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
                searchQuery = ""
            }
        ) {
            Column(
                modifier = Modifier.padding(vertical = Dimens.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
            ) {
                if (filter.isSearchable || filter.options.size > 8) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(text = stringResource(Res.string.ui_common_search_placeholder)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.paddingMedium)
                    )
                }

                if (filter.isMultiple && filter.options.size > 3) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.paddingMedium),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                onSelectionChanged(filter.options.map { it.id }.toSet())
                            }
                        ) {
                            Text(
                                text = stringResource(Res.string.ui_common_select_all),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        TextButton(
                            onClick = {
                                onSelectionChanged(emptySet())
                            }
                        ) {
                            Text(
                                text = stringResource(Res.string.ui_common_clear_all),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                filteredOptions.forEach { option ->
                    val isSelected = selectedIds.contains(option.id)
                    DropdownMenuItem(
                        text = { Text(text = option.title) },
                        leadingIcon = {
                            if (filter.isMultiple) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = null
                                )
                            } else {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = null
                                )
                            }
                        },
                        onClick = {
                            if (filter.isMultiple) {
                                val newSelection = if (isSelected) {
                                    selectedIds - option.id
                                } else {
                                    selectedIds + option.id
                                }
                                onSelectionChanged(newSelection)
                            } else {
                                onSelectionChanged(setOf(option.id))
                                isExpanded = false
                                searchQuery = ""
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListingChoiceDropdownPreview() {
    MaterialTheme {
        Surface {
            ListingChoiceDropdown(
                filter = ChoiceListingFilterDefinition(
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
                ),
                selectedIds = setOf(ListingChoiceDropdownTestData.OPTION_ACTIVE_ID),
                onSelectionChanged = {}
            )
        }
    }
}

internal object ListingChoiceDropdownTestData {
    const val FILTER_ID_STATUS = "status"
    const val FILTER_TITLE_STATUS = "Status"
    const val OPTION_ACTIVE_ID = "active"
    const val OPTION_ACTIVE_TITLE = "Active"
    const val OPTION_INACTIVE_ID = "inactive"
    const val OPTION_INACTIVE_TITLE = "Inactive"
    const val LABEL_ALL = "All"
}
