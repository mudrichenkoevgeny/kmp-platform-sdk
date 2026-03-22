package io.github.mudrichenkoevgeny.kmp.feature.user.mock.model.user

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class CurrentUserMockTest {

    @Test
    fun defaultRoleAndAccountStatus() {
        val user = mockCurrentUser()

        assertEquals(UserRole.USER, user.role)
        assertEquals(UserAccountStatus.ACTIVE, user.accountStatus)
        assertNull(user.updatedAt)
    }

    @Test
    fun passesThroughRoleAndAccountStatusParameters() {
        val role = UserRole.entries.first()
        val accountStatus = UserAccountStatus.entries.last()

        val user = mockCurrentUser(role = role, accountStatus = accountStatus)

        assertEquals(role, user.role)
        assertEquals(accountStatus, user.accountStatus)
    }

    @Test
    fun eachInvocationUsesNewUserId() {
        val first = mockCurrentUser()
        val second = mockCurrentUser()

        assertNotEquals(first.id, second.id)
    }
}
