package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort

/**
 * Definition of a sorting option available in a listing interface.
 *
 * @property id Unique identifier of the sort option.
 * @property title Display title of the sort option.
 */
public data class ListingSortDefinition(
    public val id: String,
    public val title: String
)
