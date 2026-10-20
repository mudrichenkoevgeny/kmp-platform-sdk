package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.component.user.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListTestTags

import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserSummary
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserSummary

/**
 * A list item card representing a single user summary in administrative user lists.
 *
 * @param user The [UserSummary] data to display.
 * @param onClick Callback invoked when the user card is tapped.
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param isSelf True if this card represents the current logged-in manager's account.
 */
@Composable
fun UserItem(
    user: UserSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelf: Boolean = false
) {
    val cardColors = if (isSelf) {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    } else {
        CardDefaults.cardColors()
    }
    val commonUi = LocalCommonStrings.current.ui
    val userUi = LocalUserStrings.current.ui

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(GlobalUserListTestTags.USER_ITEM_PREFIX + user.id.value),
        elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader),
        colors = cardColors
    ) {
        Column(modifier = Modifier.padding(CoreTheme.dimens.paddingMedium)) {
            CoreTitleText(
                text = "${commonUi.userId}: ${user.id.value}",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
            CoreBodyText(
                text = "${userUi.userRole}: ${user.role.name}",
                style = MaterialTheme.typography.bodySmall
            )
            CoreBodyText(
                text = "${userUi.userAccountStatus}: ${user.accountStatus.name}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@InternalApi
@Composable
private fun UserItemPreviewContent(user: UserSummary) {
    CoreTheme {
        Surface {
            Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                UserItem(
                    user = user,
                    onClick = {}
                )
            }
        }
    }
}

@InternalApi
private val defaultUserItemPreviewState = createTestUserSummary()

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun UserItemStatesPreview() {
    UserItemPreviewContent(user = defaultUserItemPreviewState)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun UserItemComponentSizePreview() {
    UserItemPreviewContent(user = defaultUserItemPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun UserItemThemePreview() {
    UserItemPreviewContent(user = defaultUserItemPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun UserItemFontScalePreview() {
    UserItemPreviewContent(user = defaultUserItemPreviewState)
}
