package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

sealed interface ListingFilterState

data class TextListingFilterState(
    val value: String
) : ListingFilterState

data class ChoiceListingFilterState(
    val selectedIds: Set<String>
) : ListingFilterState

data class BooleanListingFilterState(
    val value: Boolean
) : ListingFilterState

data class NumberListingFilterState(
    val value: Long
) : ListingFilterState
