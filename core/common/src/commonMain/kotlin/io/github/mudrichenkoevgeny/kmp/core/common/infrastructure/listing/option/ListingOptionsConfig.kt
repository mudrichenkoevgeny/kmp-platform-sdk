package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition

/**
 * Configuration containing available sort options and filters for a listing component.
 *
 * @property sortOptions List of available sorting definitions.
 * @property filters List of available filter definitions.
 */
public data class ListingOptionsConfig(
    public val sortOptions: List<ListingSortDefinition>,
    public val filters: List<ListingFilterDefinition>
)
