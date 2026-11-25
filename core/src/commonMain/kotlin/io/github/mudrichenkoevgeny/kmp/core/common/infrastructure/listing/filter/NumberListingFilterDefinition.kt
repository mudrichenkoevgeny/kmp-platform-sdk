package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

/**
 * Listing filter definition for numerical range input.
 *
 * @property id Unique identifier of the filter.
 * @property title Display title for the filter.
 * @property placeholder Input field placeholder text.
 * @property minValue Minimum allowed numerical value.
 * @property maxValue Maximum allowed numerical value.
 * @property defaultValueOnFocusLost Default numerical value to restore when input focus is lost.
 */
public data class NumberListingFilterDefinition(
    override val id: String,
    override val title: String,
    public val placeholder: String,
    public val minValue: Long? = null,
    public val maxValue: Long? = null,
    public val defaultValueOnFocusLost: Long? = null
) : ListingFilterDefinition
