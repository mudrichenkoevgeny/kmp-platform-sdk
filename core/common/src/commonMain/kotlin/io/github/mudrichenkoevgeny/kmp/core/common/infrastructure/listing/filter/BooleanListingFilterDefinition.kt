package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

import org.jetbrains.compose.resources.StringResource

/**
 * Listing filter definition for boolean (true/false) selection.
 *
 * @property id Unique identifier of the filter.
 * @property title Display title for the filter.
 * @property trueLabelRes Optional resource string for the true state label.
 * @property falseLabelRes Optional resource string for the false state label.
 */
public data class BooleanListingFilterDefinition(
    override val id: String,
    override val title: String,
    public val trueLabelRes: StringResource? = null,
    public val falseLabelRes: StringResource? = null
) : ListingFilterDefinition
