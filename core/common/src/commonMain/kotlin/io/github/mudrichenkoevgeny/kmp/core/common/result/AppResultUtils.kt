package io.github.mudrichenkoevgeny.kmp.core.common.result

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError

inline fun <T> AppResult<T>.onSuccess(block: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) block(data)
    return this
}

inline fun <T> AppResult<T>.onError(block: (AppError) -> Unit): AppResult<T> {
    if (this is AppResult.Error) block(error)
    return this
}

inline fun <T, R> AppResult<T>.flatMap(transform: (T) -> AppResult<R>): AppResult<R> {
    return when (this) {
        is AppResult.Success -> transform(data)
        is AppResult.Error -> AppResult.Error(this.error)
    }
}

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

inline fun <T, R> AppResult<T>.flatMapSuccess(transform: (T) -> AppResult<R>): AppResult<R> {
    return when (this) {
        is AppResult.Success -> transform(data)
        is AppResult.Error -> AppResult.Error(error)
    }
}