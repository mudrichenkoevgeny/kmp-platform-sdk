package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserListOwnerTest {

    @Test
    fun notifyUserDeleted_propagatesToAllActiveUserListOwnersInStack() {
        val targetUserId = UserId.generate()
        val owner1 = FakeUserListOwner()
        val owner2 = FakeUserListOwner()
        val nonOwner = Any()

        val stack = ChildStack(
            active = Child.Created(configuration = "owner1", instance = owner1),
            backStack = listOf(
                Child.Created(configuration = "nonOwner", instance = nonOwner),
                Child.Created(configuration = "owner2", instance = owner2)
            )
        )

        stack.notifyUserDeleted(targetUserId)

        assertEquals(listOf(targetUserId), owner1.deletedUserIds)
        assertEquals(listOf(targetUserId), owner2.deletedUserIds)
    }

    @Test
    fun notifyUserCreated_propagatesToAllActiveUserListOwnersInStack() {
        val owner1 = FakeUserListOwner()
        val owner2 = FakeUserListOwner()

        val stack = ChildStack(
            active = Child.Created(configuration = "owner1", instance = owner1),
            backStack = listOf(
                Child.Created(configuration = "owner2", instance = owner2)
            )
        )

        stack.notifyUserCreated()

        assertTrue(owner1.userCreatedCalled)
        assertTrue(owner2.userCreatedCalled)
    }

    private class FakeUserListOwner : UserListOwner {
        val deletedUserIds = mutableListOf<UserId>()
        var userCreatedCalled = false

        override fun onUserDeleted(userId: UserId) {
            deletedUserIds.add(userId)
        }

        override fun onUserCreated() {
            userCreatedCalled = true
        }
    }
}
