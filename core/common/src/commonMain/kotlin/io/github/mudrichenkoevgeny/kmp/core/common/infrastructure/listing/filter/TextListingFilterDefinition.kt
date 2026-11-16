package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Listing filter definition for free-form text input.
 *
 * @property id Unique identifier of the filter.
 * @property title Display title for the filter.
 * @property placeholder Input field placeholder text.
 */
public data class TextListingFilterDefinition(
    override val id: String,
    override val title: String,
    public val placeholder: String
) : ListingFilterDefinition
