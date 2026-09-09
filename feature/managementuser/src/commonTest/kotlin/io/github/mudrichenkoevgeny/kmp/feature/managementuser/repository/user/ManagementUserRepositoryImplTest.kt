package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.user.ManagementUserApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.user.userDetailsPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserDetails
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

    private val testUserId = "550e8400-e29b-41d4-a716-446655440000".toUserIdOrThrow()

    @Test
    fun createUser_mapsPayloadToDomainModel_onSuccess() = runTest {
        val payload = userDetailsPayloadMock()
        api.createUserResult = AppResult.Success(payload)

        val request = CreateByEmailRequest(
            email = "test@test.com",
            password = "password",
            role = "ADMIN",
            status = "ACTIVE",
            authorityLevel = 10,
            permissionCodes = setOf("READ", "WRITE"),
        )

        val result = repository.createUser(request)

        assertIs<AppResult.Success<*>>(result)
        assertEquals(payload.toUserDetails(), (result as AppResult.Success).data)
    }

    @Test
    fun getUsers_mapsPagedPayloadToDomainModel_onSuccess() = runTest {
        val payload = userDetailsPayloadMock()
        val pagedPayload = PagedResult(
            items = listOf(payload),
            totalCount = 1L,
            pageNumber = 1,
            pageSize = 20,
            totalPages = 1L,
        )
        api.getUsersResult = AppResult.Success(pagedPayload)

        val result = repository.getUsers()

        assertIs<AppResult.Success<*>>(result)
        val data = (result as AppResult.Success).data
        assertEquals(1, data.items.size)
        assertEquals(payload.toUserDetails(), data.items.first())
    }

    @Test
    fun getUser_mapsPayloadToDomainModel_onSuccess() = runTest {
        val payload = userDetailsPayloadMock()
        api.getUserResult = AppResult.Success(payload)

        val result = repository.getUser(testUserId)

        assertIs<AppResult.Success<*>>(result)
        assertEquals(payload.toUserDetails(), (result as AppResult.Success).data)
    }

    @Test
    fun updateUser_returnsResultFromApi() = runTest {
        api.updateUserResult = AppResult.Success(Unit)

        val result = repository.updateUser(testUserId, UpdateUserRequest())

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun deleteUser_returnsResultFromApi() = runTest {
        api.deleteUserResult = AppResult.Success(Unit)

        val result = repository.deleteUser(testUserId)

        assertIs<AppResult.Success<Unit>>(result)
    }
}
