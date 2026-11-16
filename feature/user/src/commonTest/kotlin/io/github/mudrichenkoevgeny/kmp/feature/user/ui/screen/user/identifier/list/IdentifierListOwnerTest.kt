package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import kotlin.test.Test
import kotlin.test.assertEquals

class IdentifierListOwnerTest {

    @Test
    fun notifyIdentifierDeleted_propagatesToIdentifierListOwnersInStack() {
        val targetIdentifierId = UserIdentifierId.generate()
        val owner1 = FakeIdentifierListOwner()
        val owner2 = FakeIdentifierListOwner()
        val nonOwner = Any()

        val stack = ChildStack(
            active = Child.Created(configuration = "owner1", instance = owner1),
            backStack = listOf(
                Child.Created(configuration = "nonOwner", instance = nonOwner),
                Child.Created(configuration = "owner2", instance = owner2)
            )
        )

        stack.notifyIdentifierDeleted(targetIdentifierId)

        assertEquals(listOf(targetIdentifierId), owner1.deletedIdentifierIds)
        assertEquals(listOf(targetIdentifierId), owner2.deletedIdentifierIds)
    }

    private class FakeIdentifierListOwner : IdentifierListOwner {
        val deletedIdentifierIds = mutableListOf<UserIdentifierId>()

        override fun onIdentifierDeleted(identifierId: UserIdentifierId) {
            deletedIdentifierIds.add(identifierId)
        }
    }
}
