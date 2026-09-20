package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <C : Any, T : Any> LoginRootContainer(
    stack: Value<ChildStack<C, T>>,
    onDismiss: () -> Unit,
    content: @Composable (T) -> Unit
) {
    val commonComponent = LocalCommonComponent.current

    if (commonComponent.platformRepository.getDeviceInfo().isMobileClient()) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            LoginDialogSurface(stack = stack, content = content)
        }
    } else {
        Dialog(onDismissRequest = onDismiss) {
            LoginDialogSurface(stack = stack, content = content)
        }
    }
}

@Composable
private fun <C : Any, T : Any> LoginDialogSurface(
    stack: Value<ChildStack<C, T>>,
    content: @Composable (T) -> Unit
) {
    val commonComponent = LocalCommonComponent.current
    val isMobile = commonComponent.platformRepository.getDeviceInfo().isMobileClient()

    val surfaceModifier = if (isMobile) {
        Modifier
            .fillMaxWidth()
            .height(CoreTheme.dimens.dialogHeight)
    } else {
        Modifier
            .width(CoreTheme.dimens.dialogWidth)
            .height(CoreTheme.dimens.dialogHeight)
    }

    Surface(
        modifier = surfaceModifier,
        shape = RoundedCornerShape(CoreTheme.dimens.roundedCornerShape),
        color = MaterialTheme.colorScheme.surface
    ) {
        Children(
            stack = stack,
            animation = stackAnimation(slide())
        ) { child ->
            content(child.instance)
        }
    }
}
