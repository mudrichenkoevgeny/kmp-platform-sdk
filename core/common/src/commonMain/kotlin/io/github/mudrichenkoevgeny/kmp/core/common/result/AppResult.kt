package io.github.mudrichenkoevgeny.kmp.core.common.result

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * Represents an operation result that can be either a [Success] value or an [Error].
 *
 * The error is modeled as an [AppError] to enable consistent error mapping and localization.
 */
sealed class AppResult<out T> {
    /**
     * Successful result wrapper.
     */
    data class Success<out T>(val data: T) : AppResult<T>()

    /**
     * Error result wrapper.
     */
    data class Error(val error: AppError) : AppResult<Nothing>()

    inline fun <R> fold(onSuccess: (T) -> R, onFailure: (AppError) -> R): R =
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onFailure(error)
        }
}