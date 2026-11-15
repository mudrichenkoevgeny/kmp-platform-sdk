package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Represents the active state of a specific filter applied to a listing screen.
 */
public sealed interface ListingFilterState

/**
 * Represents the state of a text-based filter (e.g., search input).
 *
 * @property value The current text query entered by the user.
 */
public data class TextListingFilterState(
    public val value: String
) : ListingFilterState

/**
 * Represents the state of a multiple or single choice filter (e.g., dropdown, chips).
 *
 * @property selectedIds The set of unique identifiers of the currently selected options.
 */
public data class ChoiceListingFilterState(
    public val selectedIds: Set<String>
) : ListingFilterState

/**
 * Represents the state of a boolean toggle filter (e.g., a switch or checkbox).
 *
 * @property value The current boolean state of the filter.
 */
public data class BooleanListingFilterState(
    public val value: Boolean
) : ListingFilterState

/**
 * Represents the state of a numerical input filter.
 *
 * @property value The current numeric value entered by the user.
 */
public data class NumberListingFilterState(
    public val value: Long
) : ListingFilterState
