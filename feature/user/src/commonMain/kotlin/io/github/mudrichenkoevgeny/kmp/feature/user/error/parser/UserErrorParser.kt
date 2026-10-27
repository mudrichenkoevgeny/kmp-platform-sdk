package io.github.mudrichenkoevgeny.kmp.feature.user.error.parser

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.kmp.core.common.time.formatEpochMillisToDateTime
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.client.naming.ClientUserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.localization.UserModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.localization.resolveTotalUserIdentifiersLimitReachedMessage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.localization.resolveUserIdentifierLimitReachedMessage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.localization.resolveUserLockedMessage

/**
 * [AppErrorParser] for user-related error codes using `shared-foundation` localization.
 */
open class UserErrorParser(
    private val localeProvider: () -> String = { KmpLocalization.currentLocale }
) : AppErrorParser {

    companion object : UserErrorParser()

    override fun parse(appError: AppError): String? {
        val args = appError.args ?: emptyMap()
        val strings = UserModuleStrings(KmpLocalization.resolver, localeProvider())

        return when (appError.code) {
            UserErrorCodes.INVALID_ACCESS_TOKEN,
            UserErrorCodes.ACCESS_TOKEN_EXPIRED,
            UserErrorCodes.INVALID_REFRESH_TOKEN,
            UserErrorCodes.INVALID_SESSION -> strings.errors.invalidSession

            UserErrorCodes.USER_BANNED -> strings.errors.banned

            UserErrorCodes.USER_LOCKED -> {
                val rawType = args[UserErrorArgs.ACCOUNT_LOCKOUT_TYPE]
                val lockoutType = if (rawType != null) AccountLockoutType.fromValueOrNull(rawType) else null
                val formattedUntil = formatEpochMillisToDateTime(args[UserErrorArgs.TEMPORARY_LOCKOUT_UNTIL])
                strings.resolveUserLockedMessage(
                    lockoutType = lockoutType,
                    formattedUntil = formattedUntil
                )
            }

            UserErrorCodes.SELF_SERVICE_UNLOCK_DISABLED -> strings.errors.selfServiceUnlockDisabled

            UserErrorCodes.USER_READ_ONLY -> strings.errors.readOnly

            UserErrorCodes.USER_SECURITY_HOLD -> strings.errors.securityHold

            UserErrorCodes.USER_PENDING_DELETION -> strings.errors.pendingDeletion

            UserErrorCodes.USER_FORBIDDEN,
            UserErrorCodes.USER_ROLE_NOT_ALLOWED,
            UserErrorCodes.USER_ILLEGAL_ACCOUNT_STATUS -> strings.errors.forbidden

            UserErrorCodes.USER_MISSING_PERMISSIONS,
            UserErrorCodes.USER_INSUFFICIENT_AUTHORITY_LEVEL -> strings.errors.missingPermissions

            UserErrorCodes.USER_NOT_FOUND -> strings.errors.notFound

            UserErrorCodes.INVALID_CREDENTIALS -> strings.errors.invalidCredentials

            UserErrorCodes.WRONG_PASSWORD -> strings.errors.wrongPassword

            UserErrorCodes.PASSWORD_SETUP_REQUIRED -> strings.errors.passwordSetupRequired

            UserErrorCodes.USER_IDENTIFIER_PASSWORD_NOT_SUPPORTED -> strings.errors.identifierPasswordNotSupported

            UserErrorCodes.USER_IDENTIFIER_PASSWORD_NOT_SET -> strings.errors.identifierPasswordNotSet

            UserErrorCodes.WRONG_CONFIRMATION_CODE -> strings.errors.wrongConfirmationCode

            UserErrorCodes.EXTERNAL_IDENTIFIER_LINKAGE_FAILED -> strings.errors.externalLinkageFailed

            UserErrorCodes.CAN_NOT_DELETE_USER_IDENTIFIER -> strings.errors.canNotDeleteIdentifier

            UserErrorCodes.CAN_NOT_CREATE_USER_IDENTIFIER -> strings.errors.canNotCreateIdentifier

            UserErrorCodes.EMAIL_NOT_ALLOWED -> strings.errors.emailNotAllowed

            UserErrorCodes.USER_IDENTIFIER_LIMIT_REACHED -> {
                val commonStrings = CommonModuleStrings(KmpLocalization.resolver, localeProvider())
                val limit = args[UserErrorArgs.MAX_NUMBER_OF_IDENTIFIERS]
                val providerRaw = args[UserErrorArgs.USER_AUTH_PROVIDER]
                strings.resolveUserIdentifierLimitReachedMessage(
                    commonStrings = commonStrings,
                    limit = limit,
                    providerRaw = providerRaw
                )
            }

            UserErrorCodes.TOTAL_USER_IDENTIFIERS_LIMIT_REACHED -> {
                val limit = args[UserErrorArgs.MAX_NUMBER_OF_IDENTIFIERS]
                strings.resolveTotalUserIdentifiersLimitReachedMessage(
                    limit = limit
                )
            }

            UserErrorCodes.REGISTRATION_DISABLED -> strings.errors.registrationDisabled

            ClientUserErrorCodes.EXTERNAL_AUTH_CANCELLED -> strings.errors.externalAuthCancelled

            ClientUserErrorCodes.EXTERNAL_AUTH_FAILED -> strings.errors.externalAuthFailed

            ClientUserErrorCodes.TOO_MANY_CONFIRMATION_REQUESTS -> strings.errors.tooManyConfirmationRequests

            else -> null
        }
    }
}
