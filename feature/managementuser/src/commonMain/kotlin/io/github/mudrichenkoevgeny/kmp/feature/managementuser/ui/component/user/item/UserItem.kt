package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.component.user.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.user_account_status
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.user_id
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.user_role
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainTestTags
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
            .testTag(UsersManagementMainTestTags.USER_ITEM_PREFIX + user.id.value),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationHeader)
    ) {
        Column(modifier = Modifier.padding(Dimens.paddingMedium)) {
            Text(
                text = "${stringResource(Res.string.user_id)}: ${user.id.value}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(Dimens.paddingSmall))
            Text(
                text = "${stringResource(Res.string.user_role)}: ${user.role.name}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${stringResource(Res.string.user_account_status)}: ${user.accountStatus.name}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun UserItemPreview() {
    MaterialTheme {
        Surface {
            UserItem(
                user = userDetailsMock(),
                onClick = {}
            )
        }
    }
}
