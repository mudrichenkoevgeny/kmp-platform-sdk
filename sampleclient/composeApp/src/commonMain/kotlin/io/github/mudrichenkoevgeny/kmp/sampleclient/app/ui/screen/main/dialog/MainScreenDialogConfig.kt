package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.dialog

import kotlinx.serialization.Serializable

/**
 * Serializable configuration hierarchy for client main screen dialog slot.
 */
@Serializable
public sealed class MainScreenDialogConfig {
    /**
     * Login dialog configuration.
     *
     * @param isDismissible whether the dialog can be dismissed.
     */
    @Serializable
    public data class Login(
        public val isDismissible: Boolean = true
    ) : MainScreenDialogConfig()
}
