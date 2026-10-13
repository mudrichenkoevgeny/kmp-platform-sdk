package io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink

internal object ManagementDeepLinkConstants {
    const val DEFAULT_BASE_HOST = "kmp-platform.internal"

    object Segment {
        const val ROOT = "management"
        const val SETTINGS = "settings"
        const val AUTH = "auth"
        const val GLOBAL = "global"
        const val SECURITY = "security"
        const val USERS = "users"
        const val CREATE = "create"
        const val SESSIONS = "sessions"
        const val IDENTIFIERS = "identifiers"
        const val AUDIT = "audit"
    }

    object Query {
        const val PARAM_ACTOR_IDS = "actorIds"
        const val PARAM_DIALOG = "dialog"
    }

    object Value {
        const val DIALOG_RESET_CONFIRM = "reset-confirm"
    }
}
