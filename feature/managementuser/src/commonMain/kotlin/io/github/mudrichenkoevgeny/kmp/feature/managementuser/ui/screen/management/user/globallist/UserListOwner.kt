package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import com.arkivanov.decompose.router.stack.ChildStack
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Interface for components or child wrappers that manage an active list of platform users.
 */
public interface UserListOwner {
    /**
     * Removes the deleted user from the active list state.
     *
     * @param userId The [UserId] of the deleted user.
     */
    public fun onUserDeleted(userId: UserId)

    /**
     * Refreshes the user list when a new user is created.
     */
    public fun onUserCreated()
}

/**
 * Finds active or previous [UserListOwner] instances in the [ChildStack] and notifies them of user deletion.
 */
public fun ChildStack<*, *>.notifyUserDeleted(userId: UserId) {
    items
        .map { it.instance }
        .filterIsInstance<UserListOwner>()
        .forEach { it.onUserDeleted(userId) }
}

/**
 * Finds active or previous [UserListOwner] instances in the [ChildStack] and notifies them of user creation.
 */
public fun ChildStack<*, *>.notifyUserCreated() {
    items
        .map { it.instance }
        .filterIsInstance<UserListOwner>()
        .forEach { it.onUserCreated() }
}
