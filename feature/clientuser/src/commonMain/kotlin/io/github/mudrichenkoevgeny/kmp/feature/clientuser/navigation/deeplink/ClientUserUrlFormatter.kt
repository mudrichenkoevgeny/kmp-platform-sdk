package io.github.mudrichenkoevgeny.kmp.feature.clientuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.UrlFormatter
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.ClientLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordComponent
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

class ClientUserUrlFormatter(
    private val baseHost: String = "kmp-platform.internal"
) : UrlFormatter<ClientLoginDestination> {

    override fun format(destination: ClientLoginDestination): String {
        val builder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = baseHost
        )

        when (destination) {
            is ClientLoginDestination.Welcome -> {
                builder.appendPathSegments("")
            }
            is ClientLoginDestination.LoginByEmail -> {
                builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.LOGIN, "email")
            }
            is ClientLoginDestination.LoginByPhone -> {
                builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.LOGIN, ClientUserDeepLinkConstants.Segment.PHONE)
                if (destination.params.isConfirmStep) {
                    builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.CONFIRM)
                }
                destination.params.phone?.let { phone ->
                    if (phone.isNotBlank()) {
                        builder.parameters.append(ClientUserDeepLinkConstants.Query.PARAM_PHONE, phone)
                    }
                }
            }
            is ClientLoginDestination.RegistrationByEmail -> {
                builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.REGISTER, ClientUserDeepLinkConstants.Segment.EMAIL)
                if (destination.params.isConfirmStep) {
                    builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.CONFIRM)
                }
                destination.params.email?.let { email ->
                    if (email.isNotBlank()) {
                        builder.parameters.append(ClientUserDeepLinkConstants.Query.PARAM_EMAIL, email)
                    }
                }
            }
            is ClientLoginDestination.ResetEmailPassword -> {
                builder.appendPathSegments("reset-password")
            }
            is ClientLoginDestination.LoginByTotp -> {
                builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.LOGIN, "totp")
                builder.parameters.append("token", destination.mfaToken)
            }
            is ClientLoginDestination.PendingDeletion -> {
                builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.LOGIN, "pending-deletion")
            }
            is ClientLoginDestination.AccountUnlock -> {
                builder.appendPathSegments("unlock")
            }
        }

        return builder.buildString()
    }

    fun formatChild(destination: ClientLoginDestination, child: ClientLoginRootComponent.Child): String {
        return when (child) {
            is ClientLoginRootComponent.Child.LoginByPhone -> {
                val activeStep = child.component.stack.value.active.instance
                val builder = URLBuilder(protocol = URLProtocol.HTTPS, host = baseHost)
                builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.LOGIN, ClientUserDeepLinkConstants.Segment.PHONE)
                when (activeStep) {
                    is LoginByPhoneComponent.Child.CodeInput -> {
                        builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.CONFIRM)
                        if (activeStep.phone.isNotBlank()) {
                            builder.parameters.append(ClientUserDeepLinkConstants.Query.PARAM_PHONE, activeStep.phone)
                        }
                    }
                    is LoginByPhoneComponent.Child.PhoneInput -> {
                        if (activeStep.initialPhone.isNotBlank()) {
                            builder.parameters.append(ClientUserDeepLinkConstants.Query.PARAM_PHONE, activeStep.initialPhone)
                        }
                    }
                }
                builder.buildString()
            }
            is ClientLoginRootComponent.Child.RegistrationByEmail -> {
                val activeStep = child.component.stack.value.active.instance
                val builder = URLBuilder(protocol = URLProtocol.HTTPS, host = baseHost)
                builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.REGISTER, ClientUserDeepLinkConstants.Segment.EMAIL)
                when (activeStep) {
                    is RegistrationByEmailComponent.Child.ConfirmInput -> {
                        builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.CONFIRM)
                        if (activeStep.email.isNotBlank()) {
                            builder.parameters.append(ClientUserDeepLinkConstants.Query.PARAM_EMAIL, activeStep.email)
                        }
                    }
                    is RegistrationByEmailComponent.Child.EmailInput -> {
                        if (activeStep.initialEmail.isNotBlank()) {
                            builder.parameters.append(ClientUserDeepLinkConstants.Query.PARAM_EMAIL, activeStep.initialEmail)
                        }
                    }
                }
                builder.buildString()
            }
            is ClientLoginRootComponent.Child.ResetEmailPassword -> {
                val activeStep = child.component.stack.value.active.instance
                val builder = URLBuilder(protocol = URLProtocol.HTTPS, host = baseHost)
                builder.appendPathSegments("reset-password")
                when (activeStep) {
                    is ResetEmailPasswordComponent.Child.ConfirmInput -> {
                        builder.appendPathSegments(ClientUserDeepLinkConstants.Segment.CONFIRM)
                        if (activeStep.email.isNotBlank()) {
                            builder.parameters.append(ClientUserDeepLinkConstants.Query.PARAM_EMAIL, activeStep.email)
                        }
                    }
                    is ResetEmailPasswordComponent.Child.EmailInput -> {
                        if (activeStep.initialEmail.isNotBlank()) {
                            builder.parameters.append(ClientUserDeepLinkConstants.Query.PARAM_EMAIL, activeStep.initialEmail)
                        }
                    }
                }
                builder.buildString()
            }
            else -> format(destination)
        }
    }
}
