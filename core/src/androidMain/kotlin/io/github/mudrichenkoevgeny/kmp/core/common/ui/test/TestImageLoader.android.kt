package io.github.mudrichenkoevgeny.kmp.core.common.ui.test

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.intercept.Interceptor
import coil3.request.ErrorResult
import kotlinx.coroutines.Dispatchers

/**
 * Android [configureTestImageLoader] implementation.
 *
 * Registers a synchronous [ImageLoader] using [Dispatchers.Unconfined] and a failing [Interceptor]
 * so image requests deterministically resolve to an error state without network calls.
 */
internal actual fun configureTestImageLoader() {
    SingletonImageLoader.setSafe { context ->
        ImageLoader.Builder(context)
            .coroutineContext(Dispatchers.Unconfined)
            .components {
                add { chain ->
                    ErrorResult(
                        image = null,
                        request = chain.request,
                        throwable = IllegalStateException("Test image load disabled")
                    )
                }
            }
            .build()
    }
}
