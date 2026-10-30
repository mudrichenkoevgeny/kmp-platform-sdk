package io.github.mudrichenkoevgeny.kmp.core.storage.ui.utils

import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.localization.UiStrings

/**
 * Utility for resolving localized strings related to storage categories.
 */
object StorageStringsMapper {

    /**
     * Returns the localized string for a given [StorageCategory].
     *
     * @param category Storage category enum.
     * @param strings Localized UI strings bundle.
     * @return Localized category label string.
     */
    fun getCategoryString(category: StorageCategory, strings: UiStrings): String {
        return when (category) {
            StorageCategory.IMAGE -> strings.categoryImage
            StorageCategory.VIDEO -> strings.categoryVideo
            StorageCategory.AUDIO -> strings.categoryAudio
            StorageCategory.DOCUMENT -> strings.categoryDocument
            StorageCategory.OTHER -> strings.categoryOther
        }
    }
}
