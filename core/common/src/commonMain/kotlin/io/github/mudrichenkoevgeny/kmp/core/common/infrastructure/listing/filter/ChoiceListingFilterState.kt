package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Represents the state of a multiple or single choice filter (e.g., dropdown, chips).
 *
 * @property selectedIds The set of unique identifiers of the currently selected options.
 */
public data class ChoiceListingFilterState(
    public val selectedIds: Set<String>
) : ListingFilterState
