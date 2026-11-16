package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Represents the state of a numerical input filter.
 *
 * @property value The current numeric value entered by the user.
 */
public data class NumberListingFilterState(
    public val value: Long
) : ListingFilterState
