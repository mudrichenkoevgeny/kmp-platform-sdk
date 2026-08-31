package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest

/**
 * Administratively creates a new user account.
 *
 * @param managementUserRepository Administrative user management API.
 */
class CreateUserUseCase(
    private val managementUserRepository: ManagementUserRepository
) {
    /**
     * @param request Payload details for creating an account via email.
     * @return Detailed information of the newly created user domain model, or a mapped failure.
     */
    suspend operator fun invoke(request: CreateByEmailRequest): AppResult<UserDetails> {
        return managementUserRepository.createUser(request)
    }
}
