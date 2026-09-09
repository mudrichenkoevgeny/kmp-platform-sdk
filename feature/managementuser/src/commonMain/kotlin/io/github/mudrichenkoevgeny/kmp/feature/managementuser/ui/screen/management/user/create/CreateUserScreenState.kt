package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

data class CreateUserScreenState(
    val email: String = "",
    val password: String = "",
    val role: String = "USER",
    val status: String = "ACTIVE",
    val authorityLevel: String = "0",
    val isLoading: Boolean = false,
    val error: AppError? = null,
)
