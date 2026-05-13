package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

/**
 * External declarations for the **Google Identity Services** (GSI) JavaScript library.
 *
 * These objects provide a type-safe Kotlin interface to the `google.accounts.id`
 * JavaScript namespace required for Web (Wasm) authentication.
 *
 * @see <a href="https://developers.google.com/identity/gsi/web/reference/js-reference">Google Identity Services Reference</a>
 */
@OptIn(ExperimentalWasmJsInterop::class)
external object google {
    object accounts {
        object id {
            /**
             * Initializes the Sign-In client based on the configuration object.
             * @param config A JavaScript object containing `client_id` and `callback`.
             */
            fun initialize(config: JsAny)

            /**
             * Displays the One Tap prompt or the account picker.
             * @param callback Optional callback to monitor the lifecycle of the prompt (display, skip, or dismiss).
             */
            fun prompt(callback: (PromptMomentNotification) -> Unit = definedExternally)

            /**
             * Revokes the OAuth grant for the specified user.
             * @param email The email address of the user whose grant is being revoked.
             * @param done Callback invoked when the revocation process is complete.
             */
            fun revoke(email: String, done: (JsAny) -> Unit)
        }
    }
}

/**
 * JavaScript interface representing the status of the Google Sign-In prompt.
 */
@OptIn(ExperimentalWasmJsInterop::class)
external interface PromptMomentNotification : JsAny {
    /** @return true if the prompt was not displayed to the user. */
    fun isNotDisplayed(): Boolean
    /** @return true if the user skipped or dismissed the prompt. */
    fun isSkippedMoment(): Boolean
    /** @return A string explanation of why the prompt was not displayed, if applicable. */
    fun getNotDisplayedReason(): String?
}

/**
 * Creates a plain JavaScript configuration object for Google Identity Services.
 *
 * @param clientId The Web OAuth 2.0 client ID.
 * @param callback The function to be executed when a credential (ID Token) is returned.
 * @return A [JsAny] object formatted as `{ client_id, callback }`.
 */
fun createGoogleConfig(clientId: String, callback: (JsAny) -> Unit): JsAny =
    js("({ client_id: clientId, callback: callback })")

/**
 * Extracts the `credential` (JWT ID Token) from the JavaScript response object.
 *
 * @param response The raw [JsAny] response received from the Google callback.
 * @return The ID Token as a [String].
 */
fun getCredentialFromJsResponse(response: JsAny): String =
    js("response.credential")