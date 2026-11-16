package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Represents the state of a text-based filter (e.g., search input).
 *
 * @property value The current text query entered by the user.
 */
public data class TextListingFilterState(
    public val value: String
) : ListingFilterState
