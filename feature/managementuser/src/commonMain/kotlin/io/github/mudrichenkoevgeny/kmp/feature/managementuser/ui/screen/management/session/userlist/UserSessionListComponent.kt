package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SessionListOwner
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId

interface UserSessionListComponent : SessionListOwner {
    val state: Value<UserSessionListScreenState>

    fun onRefresh()
    fun onLoadNextPage()
    fun onBackClick()
    fun onSessionClick(session: UserSession)
    fun onDeleteSessionClick(sessionId: String)
    fun onDeleteAllSessionsClick()
}
