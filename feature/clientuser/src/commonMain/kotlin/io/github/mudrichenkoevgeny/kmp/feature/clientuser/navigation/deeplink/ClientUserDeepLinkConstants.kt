package io.github.mudrichenkoevgeny.kmp.feature.clientuser.navigation.deeplink

internal object ClientUserDeepLinkConstants {
    object Segment {
        const val LOGIN = "login"
        const val PHONE = "phone"
        const val REGISTER = "register"
        const val EMAIL = "email"
        const val CONFIRM = "confirm"
    }

    object Query {
        const val PARAM_EMAIL = "email"
        const val PARAM_PHONE = "phone"
    }
}
