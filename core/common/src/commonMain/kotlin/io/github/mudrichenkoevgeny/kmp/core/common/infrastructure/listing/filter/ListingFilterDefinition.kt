package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

import org.jetbrains.compose.resources.StringResource

sealed interface ListingFilterDefinition {
    val id: String
    val title: String
}

enum class ChoiceFilterPresentationStyle {
    AUTO,
    CHIPS,
    DROPDOWN
}

data class TextListingFilterDefinition(
    override val id: String,
    override val title: String,
    val placeholder: String
) : ListingFilterDefinition

data class ChoiceListingFilterDefinition(
    override val id: String,
    override val title: String,
    val options: List<ListingFilterChoiceOption>,
    val isMultiple: Boolean = true,
    val isSearchable: Boolean = false,
    val presentationStyle: ChoiceFilterPresentationStyle = ChoiceFilterPresentationStyle.AUTO
) : ListingFilterDefinition

data class ListingFilterChoiceOption(
    val id: String,
    val title: String
)

data class BooleanListingFilterDefinition(
    override val id: String,
    override val title: String,
    val trueLabelRes: StringResource? = null,
    val falseLabelRes: StringResource? = null
) : ListingFilterDefinition

data class NumberListingFilterDefinition(
    override val id: String,
    override val title: String,
    val placeholder: String
) : ListingFilterDefinition
