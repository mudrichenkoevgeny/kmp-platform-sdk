package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

import org.jetbrains.compose.resources.StringResource

/**
 * Defines a generic filter option for listing infrastructure.
 */
public sealed interface ListingFilterDefinition {
    /** Unique identifier of the filter. */
    public val id: String

    /** Display title for the filter. */
    public val title: String
}

/**
 * Presentation style options for choice-based listing filters.
 */
public enum class ChoiceFilterPresentationStyle {
    /** Automatic presentation style selection based on UI layout constraints. */
    AUTO,

    /** Render as a collection of choice chips. */
    CHIPS,

    /** Render as a dropdown menu. */
    DROPDOWN
}

/**
 * Listing filter definition for free-form text input.
 *
 * @property id Unique identifier of the filter.
 * @property title Display title for the filter.
 * @property placeholder Input field placeholder text.
 */
public data class TextListingFilterDefinition(
    override val id: String,
    override val title: String,
    public val placeholder: String
) : ListingFilterDefinition

/**
 * Listing filter definition for choosing among predefined options.
 *
 * @property id Unique identifier of the filter.
 * @property title Display title for the filter.
 * @property options List of available choice options.
 * @property isMultiple Whether multiple options can be selected simultaneously.
 * @property isSearchable Whether options can be searched/filtered by text.
 * @property presentationStyle Preferred UI presentation style.
 */
public data class ChoiceListingFilterDefinition(
    override val id: String,
    override val title: String,
    public val options: List<ListingFilterChoiceOption>,
    public val isMultiple: Boolean = true,
    public val isSearchable: Boolean = false,
    public val presentationStyle: ChoiceFilterPresentationStyle = ChoiceFilterPresentationStyle.AUTO
) : ListingFilterDefinition

/**
 * Represents a single option choice within a choice listing filter.
 *
 * @property id Unique identifier of the choice option.
 * @property title Display title of the choice option.
 */
public data class ListingFilterChoiceOption(
    public val id: String,
    public val title: String
)

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
