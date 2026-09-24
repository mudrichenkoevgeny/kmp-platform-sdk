package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

/**
 * Mode determining [AuthProviderButton] text label (Sign in vs Add).
 */
enum class AuthProviderButtonMode {
    /** Label formatted for sign-in flows (e.g. "Sign in with Email"). */
    SIGN_IN,

    /** Label formatted for adding an identifier (e.g. "Add Email"). */
    ADD
}
