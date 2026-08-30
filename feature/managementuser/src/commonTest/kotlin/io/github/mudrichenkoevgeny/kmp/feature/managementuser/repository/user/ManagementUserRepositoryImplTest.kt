package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.user.ManagementUserApiMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.UpdateUserRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementUserRepositoryImplTest {

    private val api = ManagementUserApiMock()
    private val repository = ManagementUserRepositoryImpl(api)

    private val dummyPayload = UserDetailsPayload(
        id = "550e8400-e29b-41d4-a716-446655440000",
        role = "ADMIN",
        accountStatus = "ACTIVE",
        accountStatusBeforeDeletion = null,
        authorityLevel = 10,
        permissionCodes = setOf("READ", "WRITE"),
        isTotpEnabled = false,
        createdAt = 1000L
    )

    private val testUserId = "550e8400-e29b-41d4-a716-446655440000".toUserIdOrThrow()

    @Test
    fun `createUser maps payload to domain model on success`() = runTest {
        api.createUserResult = AppResult.Success(dummyPayload)

        val request = CreateByEmailRequest(
            email = "test@test.com",
            password = "password",
            role = "ADMIN",
            status = "ACTIVE",
            authorityLevel = 10,
            permissionCodes = setOf("READ", "WRITE")
        )

        val result = repository.createUser(request)

        assertIs<AppResult.Success<*>>(result)
        assertEquals(dummyPayload.toUserDetails(), (result as AppResult.Success).data)
    }

    @Test
    fun `getUsers maps paged payload to domain model on success`() = runTest {
        val pagedPayload = PagedResult(
            items = listOf(dummyPayload),
            totalCount = 1L,
            pageNumber = 1,
            pageSize = 20,
            totalPages = 1L
        )
        api.getUsersResult = AppResult.Success(pagedPayload)

        val result = repository.getUsers()

        assertIs<AppResult.Success<*>>(result)
        val data = (result as AppResult.Success).data
        assertEquals(1, data.items.size)
        assertEquals(dummyPayload.toUserDetails(), data.items.first())
    }

    @Test
    fun `getUser maps payload to domain model on success`() = runTest {
        api.getUserResult = AppResult.Success(dummyPayload)

        val result = repository.getUser(testUserId)

        assertIs<AppResult.Success<*>>(result)
        assertEquals(dummyPayload.toUserDetails(), (result as AppResult.Success).data)
    }

    @Test
    fun `updateUser returns result from api`() = runTest {
        api.updateUserResult = AppResult.Success(Unit)

        val result = repository.updateUser(testUserId, UpdateUserRequest())

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun `deleteUser returns result from api`() = runTest {
        api.deleteUserResult = AppResult.Success(Unit)

        val result = repository.deleteUser(testUserId)

        assertIs<AppResult.Success<Unit>>(result)
    }
}