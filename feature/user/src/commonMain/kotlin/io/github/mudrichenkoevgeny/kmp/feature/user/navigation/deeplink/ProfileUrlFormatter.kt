package io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.UrlFormatter
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.SelfIdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainComponent
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

class ProfileUrlFormatter(
    private val baseHost: String = ProfileDeepLinkConstants.DEFAULT_BASE_HOST
) : UrlFormatter<ProfileDestination> {

    override fun format(destination: ProfileDestination): String {
        val builder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = baseHost
        )

        when (destination) {
            is ProfileDestination.Main -> builder.appendPathSegments(ProfileDeepLinkConstants.Segment.ROOT)
            is ProfileDestination.TotpMain -> {
                builder.appendPathSegments(ProfileDeepLinkConstants.Segment.ROOT, ProfileDeepLinkConstants.Segment.TOTP)
                destination.params.token?.let { token ->
                    builder.appendPathSegments(ProfileDeepLinkConstants.Segment.SETUP)
                    builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_TOKEN, token)
                }
            }
            is ProfileDestination.TotpRecoveryCodes -> builder.appendPathSegments(ProfileDeepLinkConstants.Segment.ROOT, ProfileDeepLinkConstants.Segment.TOTP, ProfileDeepLinkConstants.Segment.RECOVERY)
            is ProfileDestination.Sessions -> builder.appendPathSegments(ProfileDeepLinkConstants.Segment.ROOT, ProfileDeepLinkConstants.Segment.SESSIONS)
            is ProfileDestination.SessionDetail -> builder.appendPathSegments(ProfileDeepLinkConstants.Segment.ROOT, ProfileDeepLinkConstants.Segment.SESSIONS, destination.sessionId.asHexDashString())
            is ProfileDestination.Identifiers -> {
                builder.appendPathSegments(ProfileDeepLinkConstants.Segment.ROOT, ProfileDeepLinkConstants.Segment.IDENTIFIERS)
                destination.params.dialog?.let { dialog ->
                    builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_DIALOG, dialog)
                }
                destination.params.step?.let { step ->
                    builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_STEP, step)
                }
                destination.params.value?.let { value ->
                    builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_EMAIL, value)
                }
            }
            is ProfileDestination.IdentifierDetail -> builder.appendPathSegments(ProfileDeepLinkConstants.Segment.ROOT, ProfileDeepLinkConstants.Segment.IDENTIFIERS, destination.identifierId.asHexDashString())
        }

        return builder.buildString()
    }

    fun formatChild(destination: ProfileDestination, child: ProfileRootComponent.Child): String {
        return when (child) {
            is ProfileRootComponent.Child.TotpMain -> {
                val activeStep = child.component.stack.value.active.instance
                if (activeStep is TotpMainComponent.Child.Setup) {
                    val builder = URLBuilder(protocol = URLProtocol.HTTPS, host = baseHost)
                    builder.appendPathSegments(ProfileDeepLinkConstants.Segment.ROOT, ProfileDeepLinkConstants.Segment.TOTP, ProfileDeepLinkConstants.Segment.SETUP)
                    if (activeStep.mfaToken.isNotBlank()) {
                        builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_TOKEN, activeStep.mfaToken)
                    }
                    builder.buildString()
                } else {
                    format(destination)
                }
            }
            is ProfileRootComponent.Child.Identifiers -> {
                val activeDialog = child.component.addIdentifierDialogSlot.value.child?.instance
                if (activeDialog != null) {
                    val builder = URLBuilder(protocol = URLProtocol.HTTPS, host = baseHost)
                    builder.appendPathSegments(ProfileDeepLinkConstants.Segment.ROOT, ProfileDeepLinkConstants.Segment.IDENTIFIERS)
                    builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_DIALOG, ProfileDeepLinkConstants.Value.DIALOG_ADD)
                    when (activeDialog) {
                        is SelfIdentifierListComponent.AddIdentifierDialogChild.ProviderSelection -> {}
                        is SelfIdentifierListComponent.AddIdentifierDialogChild.EmailInput -> {
                            builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_STEP, ProfileDeepLinkConstants.Value.STEP_EMAIL)
                            if (activeDialog.initialEmail.isNotBlank()) {
                                builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_EMAIL, activeDialog.initialEmail)
                            }
                        }
                        is SelfIdentifierListComponent.AddIdentifierDialogChild.EmailConfirm -> {
                            builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_STEP, ProfileDeepLinkConstants.Value.STEP_EMAIL_CONFIRM)
                            if (activeDialog.email.isNotBlank()) {
                                builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_EMAIL, activeDialog.email)
                            }
                        }
                        is SelfIdentifierListComponent.AddIdentifierDialogChild.PhoneInput -> {
                            builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_STEP, ProfileDeepLinkConstants.Value.STEP_PHONE)
                            if (activeDialog.initialPhone.isNotBlank()) {
                                builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_PHONE, activeDialog.initialPhone)
                            }
                        }
                        is SelfIdentifierListComponent.AddIdentifierDialogChild.PhoneConfirm -> {
                            builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_STEP, ProfileDeepLinkConstants.Value.STEP_PHONE_CONFIRM)
                            if (activeDialog.phone.isNotBlank()) {
                                builder.parameters.append(ProfileDeepLinkConstants.Query.PARAM_PHONE, activeDialog.phone)
                            }
                        }
                    }
                    builder.buildString()
                } else {
                    format(destination)
                }
            }
            else -> format(destination)
        }
    }
}
