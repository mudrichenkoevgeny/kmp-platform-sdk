package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter

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
