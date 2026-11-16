package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionListOwnerTest {

    @Test
    fun notifySessionRevoked_propagatesToSessionListOwnersInStack() {
        val targetSessionId = UserSessionId.generate()
        val owner1 = FakeSessionListOwner()
        val owner2 = FakeSessionListOwner()
        val nonOwner = Any()

        val stack = ChildStack(
            active = Child.Created(configuration = "owner1", instance = owner1),
            backStack = listOf(
                Child.Created(configuration = "nonOwner", instance = nonOwner),
                Child.Created(configuration = "owner2", instance = owner2)
            )
        )

        stack.notifySessionRevoked(targetSessionId)

        assertEquals(listOf(targetSessionId), owner1.revokedSessionIds)
        assertEquals(listOf(targetSessionId), owner2.revokedSessionIds)
    }

    private class FakeSessionListOwner : SessionListOwner {
        val revokedSessionIds = mutableListOf<UserSessionId>()

        override fun onSessionRevoked(sessionId: UserSessionId) {
            revokedSessionIds.add(sessionId)
        }
    }
}
