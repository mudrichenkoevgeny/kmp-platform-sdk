package io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink

internal object ProfileDeepLinkConstants {
    const val DEFAULT_BASE_HOST = "kmp-platform.internal"

    object Segment {
        const val ROOT = "profile"
        const val TOTP = "totp"
        const val SETUP = "setup"
        const val RECOVERY = "recovery"
        const val SESSIONS = "sessions"
        const val IDENTIFIERS = "identifiers"
        const val RESET_PASSWORD = "reset-password"
        const val CONFIRM = "confirm"
    }

    object Query {
        const val PARAM_TOKEN = "token"
        const val PARAM_DIALOG = "dialog"
        const val PARAM_STEP = "step"
        const val PARAM_EMAIL = "email"
        const val PARAM_PHONE = "phone"
    }

    object Value {
        const val DIALOG_ADD = "add"
        const val STEP_EMAIL = "email"
        const val STEP_EMAIL_CONFIRM = "email-confirm"
        const val STEP_PHONE = "phone"
        const val STEP_PHONE_CONFIRM = "phone-confirm"
    }
}
