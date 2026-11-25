package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Represents a single option choice within a choice listing filter.
 *
 * @property id Unique identifier of the choice option.
 * @property title Display title of the choice option.
 */
public data class ListingFilterChoiceOption(
    public val id: String,
    public val title: String
)
