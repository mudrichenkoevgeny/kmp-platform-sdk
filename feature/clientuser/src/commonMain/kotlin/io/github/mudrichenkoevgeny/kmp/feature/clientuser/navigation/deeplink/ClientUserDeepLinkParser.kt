package io.github.mudrichenkoevgeny.kmp.feature.clientuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.DeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.ClientLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneParams
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailParams
import io.ktor.http.Url

class ClientUserDeepLinkParser : DeepLinkParser<ClientLoginDestination> {
    override fun parse(url: Url): ClientLoginDestination? {
        val segments = url.segments.filter { it.isNotEmpty() }
        if (segments.isEmpty()) return null

        val p0 = segments.getOrNull(0)
        val p1 = segments.getOrNull(1)
        val p2 = segments.getOrNull(2)

        return when {
            p0 == ClientUserDeepLinkConstants.Segment.LOGIN && p1 == ClientUserDeepLinkConstants.Segment.PHONE -> {
                val phone = url.parameters[ClientUserDeepLinkConstants.Query.PARAM_PHONE]
                val isConfirm = p2 == ClientUserDeepLinkConstants.Segment.CONFIRM
                ClientLoginDestination.LoginByPhone(LoginByPhoneParams(phone = phone, isConfirmStep = isConfirm))
            }
            p0 == ClientUserDeepLinkConstants.Segment.REGISTER && p1 == ClientUserDeepLinkConstants.Segment.EMAIL -> {
                val email = url.parameters[ClientUserDeepLinkConstants.Query.PARAM_EMAIL]
                val isConfirm = p2 == ClientUserDeepLinkConstants.Segment.CONFIRM
                ClientLoginDestination.RegistrationByEmail(RegistrationByEmailParams(email = email, isConfirmStep = isConfirm))
            }
            else -> null
        }
    }
}
