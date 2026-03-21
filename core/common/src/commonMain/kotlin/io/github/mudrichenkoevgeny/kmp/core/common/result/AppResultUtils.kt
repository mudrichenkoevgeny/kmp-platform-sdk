package io.github.mudrichenkoevgeny.kmp.core.common.result

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError

/**
 * Executes [block] when this [AppResult] is [AppResult.Success].
 *
 * The receiver is returned to make the call chain convenient.
 */
inline fun <T> AppResult<T>.onSuccess(block: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) block(data)
    return this
}

/**
 * Executes [block] when this [AppResult] is [AppResult.Error].
 *
 * The receiver is returned to make the call chain convenient.
 */
inline fun <T> AppResult<T>.onError(block: (AppError) -> Unit): AppResult<T> {
    if (this is AppResult.Error) block(error)
    return this
}

/**
 * Transforms a successful value into another [AppResult].
 */
inline fun <T, R> AppResult<T>.flatMap(transform: (T) -> AppResult<R>): AppResult<R> {
    return when (this) {
        is AppResult.Success -> transform(data)
        is AppResult.Error -> AppResult.Error(this.error)
    }
}

/**
 * Maps a successful value into a different type.
 *
 * Exceptions thrown from [transform] are mapped into [CommonError.ContractViolation].
 */
inline fun <T, R> AppResult<T>.mapSuccess(transform: (T) -> R): AppResult<R> {
    return when (this) {
        is AppResult.Success -> {
            try {
                AppResult.Success(transform(data))
            } catch (e: Exception) {
                AppResult.Error(CommonError.ContractViolation(e))
            }
        }
        is AppResult.Error -> AppResult.Error(error)
    }
}

/**
 * Executes [transform] only for successful values, while propagating errors unchanged.
 */
inline fun <T, R> AppResult<T>.flatMapSuccess(transform: (T) -> AppResult<R>): AppResult<R> {
    return when (this) {
        is AppResult.Success -> transform(data)
        is AppResult.Error -> AppResult.Error(error)
    }
}