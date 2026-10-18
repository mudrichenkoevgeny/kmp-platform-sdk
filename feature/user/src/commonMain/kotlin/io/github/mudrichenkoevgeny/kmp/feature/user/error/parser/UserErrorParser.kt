package io.github.mudrichenkoevgeny.kmp.feature.user.error.parser

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.kmp.core.common.time.formatEpochMillisToDateTime
import io.github.mudrichenkoevgeny.kmp.feature.user.error.naming.ClientUserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.localization.UserModuleStrings

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
            UserErrorCodes.INVALID_ACCESS_TOKEN -> strings.errors.invalidAccessToken

            UserErrorCodes.ACCESS_TOKEN_EXPIRED -> strings.errors.accessTokenExpired

            UserErrorCodes.INVALID_REFRESH_TOKEN -> strings.errors.invalidRefreshToken

            UserErrorCodes.INVALID_SESSION -> strings.errors.invalidSession

            UserErrorCodes.USER_BANNED -> strings.errors.banned

            UserErrorCodes.USER_LOCKED -> {
                val formattedUntil = formatEpochMillisToDateTime(args[UserErrorArgs.TEMPORARY_LOCKOUT_UNTIL])
                if (formattedUntil != null) {
                    strings.errors.locked(formattedUntil)
                } else {
                    strings.errors.lockedWithoutArgs
                }
            }

            UserErrorCodes.SELF_SERVICE_UNLOCK_DISABLED -> strings.errors.selfServiceUnlockDisabled

            UserErrorCodes.USER_READ_ONLY -> strings.errors.readOnly

            UserErrorCodes.USER_SECURITY_HOLD -> strings.errors.securityHold

            UserErrorCodes.USER_PENDING_DELETION -> strings.errors.pendingDeletion

            UserErrorCodes.USER_FORBIDDEN -> strings.errors.forbidden

            UserErrorCodes.USER_ROLE_NOT_ALLOWED -> strings.errors.roleNotAllowed

            UserErrorCodes.USER_MISSING_PERMISSIONS -> strings.errors.missingPermissions

            UserErrorCodes.USER_ILLEGAL_ACCOUNT_STATUS -> strings.errors.illegalStatus

            UserErrorCodes.USER_INSUFFICIENT_AUTHORITY_LEVEL -> strings.errors.insufficientAuthority

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
                val provider = args[UserErrorArgs.USER_AUTH_PROVIDER]
                val limit = args[UserErrorArgs.MAX_NUMBER_OF_IDENTIFIERS]
                if (!provider.isNullOrBlank() && !limit.isNullOrBlank()) {
                    strings.errors.identifierLimitReached(limit, provider)
                } else {
                    strings.errors.identifierLimitReachedWithoutArgs
                }
            }

            UserErrorCodes.TOTAL_USER_IDENTIFIERS_LIMIT_REACHED -> {
                val limit = args[UserErrorArgs.MAX_NUMBER_OF_IDENTIFIERS]
                if (!limit.isNullOrBlank()) {
                    strings.errors.totalIdentifiersLimitReached(limit)
                } else {
                    strings.errors.totalIdentifiersLimitReachedWithoutArgs
                }
            }

            ClientUserErrorCodes.REGISTRATION_DISABLED -> strings.errors.registrationDisabled

            else -> null
        }
    }
}
