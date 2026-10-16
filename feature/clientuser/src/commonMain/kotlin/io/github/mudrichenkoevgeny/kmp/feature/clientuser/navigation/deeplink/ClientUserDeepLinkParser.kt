package io.github.mudrichenkoevgeny.kmp.feature.clientuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.DeepLinkParser
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.login.email.LoginByEmailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.login.phone.LoginByPhoneParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.login.totp.LoginByTotpParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.resetpassword.ResetPasswordParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.methodselection.UnlockMethodSelectionParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.client.auth.registration.email.ClientRegistrationByEmailParams
import io.ktor.http.Url

class ClientUserDeepLinkParser : DeepLinkParser<ClientAuthNavigation> {
    override fun parse(url: Url): ClientAuthNavigation? {
        val queryMap: Map<String, Any?> = url.parameters.entries().associate { (key, values) ->
            key to if (values.size == 1) values.first() else values
        }

        val path = url.encodedPath.trimEnd('/')

        return when (path) {
            ClientAuthNavigation.BASE_AUTH_ROUTE,
            ClientAuthNavigation.Main.ROUTE -> ClientAuthNavigation.Main
            ClientAuthNavigation.LoginByEmail.ROUTE -> ClientAuthNavigation.LoginByEmail(LoginByEmailParams.parse(queryMap))
            ClientAuthNavigation.LoginByPhone.ROUTE -> ClientAuthNavigation.LoginByPhone(LoginByPhoneParams.parse(queryMap))
            ClientAuthNavigation.Registration.ROUTE -> ClientAuthNavigation.Registration(ClientRegistrationByEmailParams.parse(queryMap))
            ClientAuthNavigation.LoginByTotp.ROUTE -> ClientAuthNavigation.LoginByTotp(LoginByTotpParams.parse(queryMap))
            ClientAuthNavigation.PendingDeletion.ROUTE -> ClientAuthNavigation.PendingDeletion
            ClientAuthNavigation.ResetPassword.ROUTE -> ClientAuthNavigation.ResetPassword(ResetPasswordParams.parse(queryMap))
            ClientAuthNavigation.UnlockMethodSelection.ROUTE -> ClientAuthNavigation.UnlockMethodSelection(UnlockMethodSelectionParams.parse(queryMap))
            else -> null
        }
    }
}
