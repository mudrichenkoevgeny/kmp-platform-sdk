package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ic_back
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview.StorageFileImagePreview
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.rememberFilePickerLauncher
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.edit.UserEditComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.edit.UserEditPreviewProvider
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

object UserEditTestTags {
    const val BACK_BUTTON = "UserEdit_BackButton"
    const val TITLE = "UserEdit_Title"
    const val NICKNAME_INPUT = "UserEdit_NicknameInput"
    const val DISPLAY_NAME_INPUT = "UserEdit_DisplayNameInput"
    const val AVATAR_CHANGE_BUTTON = "UserEdit_AvatarChangeButton"
    const val BIRTHDATE_INPUT = "UserEdit_BirthdateInput"
    const val SAVE_BUTTON = "UserEdit_SaveButton"
    const val RETRY_BUTTON = "UserEdit_RetryButton"
    const val GLOBAL_ERROR_TEXT = "UserEdit_GlobalErrorText"
}

/**
 * Screen composable for editing the authenticated user's profile details (nickname, display name, avatar, birthdate).
 *
 * @param component The Decompose [UserEditComponent] controlling state and callbacks.
 * @param modifier Optional [Modifier] for screen container layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditScreen(
    component: UserEditComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.subscribeAsState()
    val userUi = LocalUserStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui

    val filePickerLauncher = rememberFilePickerLauncher { selectedFiles ->
        val firstFile = selectedFiles.firstOrNull()
        if (firstFile != null) {
            component.onAvatarFileSelected(firstFile)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    CoreTitleText(
                        text = userUi.editProfileTitle,
                        modifier = Modifier.testTag(UserEditTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(UserEditTestTags.BACK_BUTTON)
                    ) {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_back),
                            contentDescription = commonUi.back
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val current = state) {
                is UserEditScreenState.Loading -> {
                    FullscreenLoading()
                }
                is UserEditScreenState.Content -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        UserEditContent(
                            state = current,
                            onNicknameChanged = component::onNicknameChanged,
                            onDisplayNameChanged = component::onDisplayNameChanged,
                            onBirthdateChanged = component::onBirthdateChanged,
                            onChangeAvatarClick = { filePickerLauncher.launch() },
                            onSaveClick = component::onSaveClick
                        )

                        if (current.isSaving) {
                            FullscreenOverlayLoading()
                        }
                    }
                }
                is UserEditScreenState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CoreErrorText(
                            text = current.error.toLocalizedMessage(),
                            modifier = Modifier.testTag(UserEditTestTags.GLOBAL_ERROR_TEXT)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserEditContent(
    state: UserEditScreenState.Content,
    onNicknameChanged: (String) -> Unit,
    onDisplayNameChanged: (String) -> Unit,
    onBirthdateChanged: (String) -> Unit,
    onChangeAvatarClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val userUi = LocalUserStrings.current.ui

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(CoreTheme.dimens.paddingMedium)
    ) {
        AnimatedVisibility(
            visible = state.actionError != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            state.actionError?.let { error ->
                Column {
                    CoreErrorText(
                        text = error.toLocalizedMessage(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingMedium))
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(CoreTheme.dimens.paddingMedium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CoreTitleText(
                    text = userUi.personalInformation,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingLarge))

                // Circular Avatar Container
                val avatarSourceUrl = state.selectedAvatarPayload?.localUri ?: state.avatarUrl
                Surface(
                    modifier = Modifier
                        .size(CoreTheme.dimens.previewContainerHeight)
                        .clip(CircleShape)
                        .border(
                            width = CoreTheme.dimens.borderWidthThick,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = CircleShape
                        ),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = CircleShape
                ) {
                    if (!avatarSourceUrl.isNullOrBlank()) {
                        StorageFileImagePreview(
                            imageUrl = avatarSourceUrl,
                            category = StorageCategory.IMAGE,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(CommonRes.drawable.ic_back),
                                contentDescription = null,
                                modifier = Modifier.size(CoreTheme.dimens.iconSizeXLarge),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingSmall))

                CoreButton(
                    text = "${userUi.editProfileTitle} (${userUi.avatar})",
                    onClick = onChangeAvatarClick,
                    enabled = !state.isSaving,
                    modifier = Modifier.testTag(UserEditTestTags.AVATAR_CHANGE_BUTTON)
                )

                Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingLarge))

                CoreOutlinedTextField(
                    value = state.nicknameInput,
                    onValueChange = onNicknameChanged,
                    label = { CoreBodyText(userUi.userLabel) },
                    enabled = !state.isSaving,
                    isError = state.nicknameError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(UserEditTestTags.NICKNAME_INPUT)
                )
                if (state.nicknameError != null) {
                    Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingExtraSmall))
                    CoreErrorText(
                        text = state.nicknameError.toLocalizedMessage(),
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingMedium))

                CoreOutlinedTextField(
                    value = state.displayNameInput,
                    onValueChange = onDisplayNameChanged,
                    label = { CoreBodyText(userUi.user) },
                    enabled = !state.isSaving,
                    isError = state.displayNameError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(UserEditTestTags.DISPLAY_NAME_INPUT)
                )
                if (state.displayNameError != null) {
                    Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingExtraSmall))
                    CoreErrorText(
                        text = state.displayNameError.toLocalizedMessage(),
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingMedium))

                CoreOutlinedTextField(
                    value = state.birthdateInput,
                    onValueChange = onBirthdateChanged,
                    label = { CoreBodyText(userUi.birthdate) },
                    enabled = !state.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(UserEditTestTags.BIRTHDATE_INPUT)
                )

                Spacer(modifier = Modifier.height(CoreTheme.dimens.paddingLarge))

                CoreButton(
                    text = if (state.isSaving) userUi.saving else userUi.saveProfile,
                    onClick = onSaveClick,
                    enabled = !state.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(UserEditTestTags.SAVE_BUTTON)
                )
            }
        }
    }
}

@InternalApi
@Composable
private fun UserEditScreenPreviewContent(state: UserEditScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        UserEditScreen(
            component = UserEditComponentMock(initialState = state)
        )
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview(
    @PreviewParameter(UserEditPreviewProvider::class) state: UserEditScreenState
) {
    ScreenPreviewContainer {
        UserEditScreenPreviewContent(state = state)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview(
    @PreviewParameter(UserEditPreviewProvider::class) state: UserEditScreenState
) {
    ScreenPreviewContainer {
        UserEditScreenPreviewContent(state = state)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview(
    @PreviewParameter(UserEditPreviewProvider::class) state: UserEditScreenState
) {
    ScreenPreviewContainer {
        UserEditScreenPreviewContent(state = state)
    }
}
