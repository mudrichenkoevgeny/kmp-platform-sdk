package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.settings

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AvailableAuthProvidersResponse
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class AvailableAuthProvidersMapperTest {

    @Test
    fun dropsUnknownProviderStrings() {
        val response = AvailableAuthProvidersResponse(
            primary = listOf("not-a-real-provider", "another-unknown"),
            secondary = listOf("__unknown__")
        )

        val mapped = response.toAvailableAuthProviders()

        assertTrue(mapped.primary.isEmpty())
        assertTrue(mapped.secondary.isEmpty())
    }

    @Test
    fun mapsKnownWireValuesAndPreservesOrder() {
        val knownWires = UserAuthProvider.entries.map { it.name }

        val response = AvailableAuthProvidersResponse(
            primary = listOf(knownWires[0], "unknown-x", knownWires[1]),
            secondary = listOf(knownWires[2])
        )

        val mapped = response.toAvailableAuthProviders()

        assertContentEquals(
            listOf(UserAuthProvider.entries[0], UserAuthProvider.entries[1]),
            mapped.primary
        )
        assertContentEquals(
            listOf(UserAuthProvider.entries[2]),
            mapped.secondary
        )
    }
}
