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
     *
     * @param data The successful payload.
     */
    data class Success<out T>(val data: T) : AppResult<T>()

    /**
     * Error result wrapper.
     *
     * @param error The failure details.
     */
    data class Error(val error: AppError) : AppResult<Nothing>()

    /**
     * Standard transformation function that handles both possible states of the result.
     *
     * @param onSuccess Callback for [Success] state.
     * @param onFailure Callback for [Error] state.
     * @return Result of the transformation.
     */
    inline fun <R> fold(onSuccess: (T) -> R, onFailure: (AppError) -> R): R =
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onFailure(error)
        }
}