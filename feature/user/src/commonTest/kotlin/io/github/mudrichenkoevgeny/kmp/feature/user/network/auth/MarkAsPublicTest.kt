package io.github.mudrichenkoevgeny.kmp.feature.user.network.auth

import io.ktor.client.request.HttpRequestBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MarkAsPublicTest {

    @Test
    fun markAsPublic_setsIsPublicApiAttribute() {
        val builder = HttpRequestBuilder()
        assertNull(builder.attributes.getOrNull(IsPublicApi))

        builder.markAsPublic()

        assertEquals(true, builder.attributes.getOrNull(IsPublicApi))
    }
}
