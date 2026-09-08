package io.github.mudrichenkoevgeny.kmp.core.security.mock.passwordpolicy.validator

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.ManagementPasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.OpenPasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicyValidatorResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator

@InternalApi
class PasswordPolicyValidatorMock : PasswordPolicyValidator {

    var validateResult: PasswordPolicyValidatorResult = PasswordPolicyValidatorResult.Success

    override fun validate(
        openPasswordPolicy: OpenPasswordPolicy,
        password: String
    ): PasswordPolicyValidatorResult {
        return validateResult
    }

    override fun validate(
        managementPasswordPolicy: ManagementPasswordPolicy,
        password: String
    ): PasswordPolicyValidatorResult {
        return validateResult
    }
}
