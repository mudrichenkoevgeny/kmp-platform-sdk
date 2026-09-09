package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
sealed class UsersManagementDestination {
    @Serializable
    object Main : UsersManagementDestination()

    @Serializable
    data class Detail(val userIdValue: String) : UsersManagementDestination() {
        val userId: UserId get() = UserId(Uuid.parse(userIdValue))
    }

    @Serializable
    object Create : UsersManagementDestination()

    @Serializable
    data class Sessions(val userIdValue: String) : UsersManagementDestination() {
        val userId: UserId get() = UserId(Uuid.parse(userIdValue))
    }

    @Serializable
    data class Identifiers(val userIdValue: String) : UsersManagementDestination() {
        val userId: UserId get() = UserId(Uuid.parse(userIdValue))
    }
}
