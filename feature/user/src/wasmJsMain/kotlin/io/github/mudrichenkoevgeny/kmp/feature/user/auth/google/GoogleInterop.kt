package io.github.mudrichenkoevgeny.kmp.feature.user.auth.google

@OptIn(ExperimentalWasmJsInterop::class)
external object google {
    object accounts {
        object id {
            fun initialize(config: JsAny)
            fun prompt(callback: (PromptMomentNotification) -> Unit = definedExternally)
            fun revoke(email: String, done: (JsAny) -> Unit)
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
external interface PromptMomentNotification : JsAny {
    fun isNotDisplayed(): Boolean
    fun isSkippedMoment(): Boolean
    fun getNotDisplayedReason(): String?
}

fun createGoogleConfig(clientId: String, callback: (JsAny) -> Unit): JsAny =
    js("({ client_id: clientId, callback: callback })")

fun getCredentialFromJsResponse(response: JsAny): String =
    js("response.credential")