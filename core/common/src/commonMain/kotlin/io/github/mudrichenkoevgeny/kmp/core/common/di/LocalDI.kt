package io.github.mudrichenkoevgeny.kmp.core.common.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalCommonComponent = staticCompositionLocalOf<CommonComponent> {
    error("CommonComponent not provided")
}