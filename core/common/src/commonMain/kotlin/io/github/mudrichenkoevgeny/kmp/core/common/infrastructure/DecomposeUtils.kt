package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.resume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

@Composable
fun rememberComponentContext(): ComponentContext {
    val lifecycle = remember { LifecycleRegistry() }

    DisposableEffect(Unit) {
        lifecycle.resume()
        onDispose { lifecycle.destroy() }
    }

    return remember { DefaultComponentContext(lifecycle) }
}

fun ComponentContext.componentCoroutineScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    lifecycle.doOnDestroy { scope.cancel() }
    return scope
}

fun <T : Any> Flow<T>.asValue(
    initialValue: T,
    lifecycle: Lifecycle,
    context: CoroutineContext = Dispatchers.Main.immediate
): Value<T> {
    val mutableValue = MutableValue(initialValue)
    val scope = CoroutineScope(context + SupervisorJob())

    this.onEach { mutableValue.value = it }
        .launchIn(scope)

    lifecycle.doOnDestroy { scope.cancel() }

    return mutableValue
}