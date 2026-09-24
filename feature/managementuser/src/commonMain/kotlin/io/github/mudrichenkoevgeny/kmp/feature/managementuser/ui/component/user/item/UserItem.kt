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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist.GlobalUserListTestTags
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.user_account_status
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.user_id
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.user_role
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserItem(
    user: UserDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(GlobalUserListTestTags.USER_ITEM_PREFIX + user.id.value),
        elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader)
    ) {
        Column(modifier = Modifier.padding(CoreTheme.dimens.paddingMedium)) {
            CoreTitleText(
                text = "${stringResource(Res.string.user_id)}: ${user.id.value}",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
            CoreBodyText(
                text = "${stringResource(Res.string.user_role)}: ${user.role.name}",
                style = MaterialTheme.typography.bodySmall
            )
            CoreBodyText(
                text = "${stringResource(Res.string.user_account_status)}: ${user.accountStatus.name}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@InternalApi
@Composable
private fun UserItemPreviewContent(user: UserDetails) {
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
private val defaultUserItemPreviewState = userDetailsMock()

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
