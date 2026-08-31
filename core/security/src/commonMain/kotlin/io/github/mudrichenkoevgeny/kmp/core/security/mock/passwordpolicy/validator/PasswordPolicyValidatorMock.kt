package io.github.mudrichenkoevgeny.kmp.core.security.mock.passwordpolicy.validator

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicyValidatorResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator

@InternalApi
class PasswordPolicyValidatorMock : PasswordPolicyValidator {

    var validateResult: PasswordPolicyValidatorResult = PasswordPolicyValidatorResult.Success

    override fun validate(passwordPolicy: PasswordPolicy, password: String): PasswordPolicyValidatorResult {
        return validateResult
    }
}
