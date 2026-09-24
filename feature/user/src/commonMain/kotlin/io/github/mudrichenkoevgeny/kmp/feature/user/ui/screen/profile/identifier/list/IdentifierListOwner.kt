package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list

import com.arkivanov.decompose.router.stack.ChildStack
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

/**
 * Interface for components or child wrappers that manage an active list of user identifiers.
 */
interface IdentifierListOwner {
    /**
     * Removes or refreshes the identifier list when an identifier is deleted.
     *
     * @param identifierId The [UserIdentifierId] of the deleted identifier.
     */
    fun onIdentifierDeleted(identifierId: UserIdentifierId)
}

/**
 * Finds the active or previous [IdentifierListOwner] in the [ChildStack] and notifies it of identifier deletion.
 */
fun ChildStack<*, *>.notifyIdentifierDeleted(identifierId: UserIdentifierId) {
    items
        .map { it.instance }
        .filterIsInstance<IdentifierListOwner>()
        .lastOrNull()
        ?.onIdentifierDeleted(identifierId)
}
