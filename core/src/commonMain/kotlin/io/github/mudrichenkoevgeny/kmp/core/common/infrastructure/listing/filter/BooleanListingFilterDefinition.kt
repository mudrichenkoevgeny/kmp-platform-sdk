package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Listing filter definition for boolean (true/false) selection.
 *
 * @property id Unique identifier of the filter.
 * @property title Display title for the filter.
 * @property trueLabel Optional localized string for the true state label.
 * @property falseLabel Optional localized string for the false state label.
 */
public data class BooleanListingFilterDefinition(
    override val id: String,
    override val title: String,
    public val trueLabel: String? = null,
    public val falseLabel: String? = null
) : ListingFilterDefinition
