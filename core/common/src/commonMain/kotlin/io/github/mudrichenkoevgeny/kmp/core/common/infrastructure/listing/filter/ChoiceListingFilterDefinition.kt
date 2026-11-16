package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Listing filter definition for choosing among predefined options.
 *
 * @property id Unique identifier of the filter.
 * @property title Display title for the filter.
 * @property options List of available choice options.
 * @property isMultiple Whether multiple options can be selected simultaneously.
 * @property isSearchable Whether options can be searched/filtered by text.
 * @property presentationStyle Preferred UI presentation style.
 */
public data class ChoiceListingFilterDefinition(
    override val id: String,
    override val title: String,
    public val options: List<ListingFilterChoiceOption>,
    public val isMultiple: Boolean = true,
    public val isSearchable: Boolean = false,
    public val presentationStyle: ChoiceFilterPresentationStyle = ChoiceFilterPresentationStyle.AUTO
) : ListingFilterDefinition
