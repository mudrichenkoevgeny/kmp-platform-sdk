package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.home

import com.arkivanov.decompose.ComponentContext

/**
 * Default [HomeScreenComponent] implementation with Decompose [ComponentContext] only.
 *
 * @param componentContext Lifecycle context for the home child.
 */
class HomeScreenComponentImpl(
    componentContext: ComponentContext,
) : HomeScreenComponent, ComponentContext by componentContext