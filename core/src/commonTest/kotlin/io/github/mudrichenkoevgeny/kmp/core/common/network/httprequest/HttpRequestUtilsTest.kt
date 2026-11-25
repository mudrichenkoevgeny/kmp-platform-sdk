package io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest

import io.ktor.client.request.HttpRequestBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HttpRequestUtilsTest {

    @Test
    fun markAsPublic_setsIsPublicApiAttribute() {
        val builder = HttpRequestBuilder()
        assertNull(builder.attributes.getOrNull(IsPublicApi))

        builder.markAsPublic()

        assertEquals(true, builder.attributes.getOrNull(IsPublicApi))
    }
}
