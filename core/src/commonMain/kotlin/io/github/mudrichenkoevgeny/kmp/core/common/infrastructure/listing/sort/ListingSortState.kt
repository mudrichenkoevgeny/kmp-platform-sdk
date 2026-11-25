package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort

/**
 * Represents the active sorting configuration applied to a listing screen.
 *
 * @property optionId The unique identifier of the selected sort option (e.g., column name or sorting key).
 * @property isAscending Indicates the sort direction; `true` for ascending order, `false` for descending order.
 */
public data class ListingSortState(
    public val optionId: String,
    public val isAscending: Boolean
)
