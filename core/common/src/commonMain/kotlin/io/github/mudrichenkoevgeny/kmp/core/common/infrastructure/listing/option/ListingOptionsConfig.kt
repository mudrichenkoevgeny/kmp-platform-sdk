package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition

data class ListingOptionsConfig(
    val sortOptions: List<ListingSortDefinition>,
    val filters: List<ListingFilterDefinition>
)
