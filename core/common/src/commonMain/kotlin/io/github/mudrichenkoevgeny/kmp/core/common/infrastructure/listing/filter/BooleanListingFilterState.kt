package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Represents the state of a boolean toggle filter (e.g., a switch or checkbox).
 *
 * @property value The current boolean state of the filter.
 */
public data class BooleanListingFilterState(
    public val value: Boolean
) : ListingFilterState
