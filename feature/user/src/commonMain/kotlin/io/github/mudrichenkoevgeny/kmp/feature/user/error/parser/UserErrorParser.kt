package io.github.mudrichenkoevgeny.kmp.feature.user.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.resolveLocalizedString
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorArgs
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * [AppErrorParser] for user-related error codes.
 *
 * Handles authentication tokens, session states, account status restrictions,
 * permissions, and identifier management (limits for auth providers).
 */
object UserErrorParser : AppErrorParser {

    @Composable
    override fun parse(appError: AppError): String? {
        val args = appError.args ?: emptyMap()

        return when (appError.code) {
            UserErrorCodes.INVALID_ACCESS_TOKEN ->
                stringResource(Res.string.error_user_invalid_access_token)

            UserErrorCodes.ACCESS_TOKEN_EXPIRED ->
                stringResource(Res.string.error_user_access_token_expired)

            UserErrorCodes.INVALID_REFRESH_TOKEN ->
                stringResource(Res.string.error_user_invalid_refresh_token)

            UserErrorCodes.INVALID_SESSION ->
                stringResource(Res.string.error_user_invalid_session)

            UserErrorCodes.USER_BLOCKED ->
                stringResource(Res.string.error_user_blocked)

            UserErrorCodes.USER_READ_ONLY ->
                stringResource(Res.string.error_user_read_only)

            UserErrorCodes.USER_SECURITY_HOLD ->
                stringResource(Res.string.error_user_security_hold)

            UserErrorCodes.USER_PENDING_DELETION ->
                stringResource(Res.string.error_user_pending_deletion)

            UserErrorCodes.USER_FORBIDDEN ->
                stringResource(Res.string.error_user_forbidden)

            UserErrorCodes.USER_ROLE_NOT_ALLOWED ->
                stringResource(Res.string.error_user_role_not_allowed)

            UserErrorCodes.USER_MISSING_PERMISSIONS ->
                stringResource(Res.string.error_user_missing_permissions)

            UserErrorCodes.USER_ILLEGAL_ACCOUNT_STATUS ->
                stringResource(Res.string.error_user_illegal_status)

            UserErrorCodes.USER_INSUFFICIENT_AUTHORITY_LEVEL ->
                stringResource(Res.string.error_user_insufficient_authority)

            UserErrorCodes.USER_NOT_FOUND ->
                stringResource(Res.string.error_user_not_found)

            UserErrorCodes.INVALID_CREDENTIALS ->
                stringResource(Res.string.error_user_invalid_credentials)

            UserErrorCodes.WRONG_PASSWORD ->
                stringResource(Res.string.error_user_wrong_password)

            UserErrorCodes.WRONG_CONFIRMATION_CODE ->
                stringResource(Res.string.error_user_wrong_confirmation_code)

            UserErrorCodes.EXTERNAL_IDENTIFIER_LINKAGE_FAILED ->
                stringResource(Res.string.error_user_external_linkage_failed)

            UserErrorCodes.CAN_NOT_DELETE_USER_IDENTIFIER ->
                stringResource(Res.string.error_user_can_not_delete_identifier)

            UserErrorCodes.CAN_NOT_CREATE_USER_IDENTIFIER ->
                stringResource(Res.string.error_user_can_not_create_identifier)

            UserErrorCodes.USER_IDENTIFIER_LIMIT_REACHED -> resolveLimit(
                args = args,
                providerKey = UserErrorArgs.USER_AUTH_PROVIDER,
                limitKey = UserErrorArgs.MAX_NUMBER_OF_IDENTIFIERS,
                withArgsRes = Res.string.error_user_identifier_limit_reached_args,
                fallbackRes = Res.string.error_user_identifier_limit_reached
            )

            UserErrorCodes.TOTAL_USER_IDENTIFIERS_LIMIT_REACHED -> resolveLocalizedString(
                args = args,
                key = UserErrorArgs.MAX_NUMBER_OF_IDENTIFIERS,
                withArgsRes = Res.string.error_user_total_identifiers_limit_reached_args,
                fallbackRes = Res.string.error_user_total_identifiers_limit_reached
            )

            else -> null
        }
    }

    /**
     * Resolves a localized limit error message that requires both a provider name and a limit value.
     *
     * @param args Error arguments.
     * @param providerKey Key for the auth provider name (e.g. "GOOGLE").
     * @param limitKey Key for the numeric limit value.
     * @param withArgsRes Resource to use when both arguments are present.
     * @param fallbackRes Resource to use when arguments are missing.
     */
    @Composable
    private fun resolveLimit(
        args: Map<String, String>,
        providerKey: String,
        limitKey: String,
        withArgsRes: StringResource,
        fallbackRes: StringResource
    ): String {
        val provider = args[providerKey]
        val limit = args[limitKey]
        return if (!provider.isNullOrBlank() && !limit.isNullOrBlank()) {
            stringResource(withArgsRes, limit, provider)
        } else {
            stringResource(fallbackRes)
        }
    }
}
