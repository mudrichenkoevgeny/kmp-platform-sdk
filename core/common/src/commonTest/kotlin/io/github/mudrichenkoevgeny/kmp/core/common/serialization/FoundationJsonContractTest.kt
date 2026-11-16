package io.github.mudrichenkoevgeny.kmp.core.common.serialization

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.SocketFrame
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FoundationJsonContractTest {

    @Test
    fun errorId_serializesAndDeserializesCorrectly() {
        val errorId = ErrorId.generate()

        val jsonString = FoundationJson.encodeToString(ErrorId.serializer(), errorId)
        val decoded = FoundationJson.decodeFromString(ErrorId.serializer(), jsonString)

        assertEquals(errorId, decoded)
        assertTrue(jsonString.contains(errorId.asHexDashString()))
    }

    @Test
    fun socketFrame_serializesToExpectedWireFormat_andDeserializesCorrectly() {
        val payload = JsonObject(mapOf("status" to JsonPrimitive("OK")))
        val socketFrame = SocketFrame(
            id = "00000000-0000-0000-0000-000000000001",
            type = "TEST_EVENT",
            payload = payload,
            metadata = mapOf("client" to "kmp-platform-sdk"),
            timestamp = 1700000000000L
        )

        val jsonString = FoundationJson.encodeToString(SocketFrame.serializer(), socketFrame)
        val jsonObject = FoundationJson.parseToJsonElement(jsonString).jsonObject

        assertTrue(jsonObject.containsKey("id"), "JSON must contain 'id' key")
        assertTrue(jsonObject.containsKey("type"), "JSON must contain 'type' key")
        assertTrue(jsonObject.containsKey("payload"), "JSON must contain 'payload' key")
        assertTrue(jsonObject.containsKey("metadata"), "JSON must contain 'metadata' key")
        assertTrue(jsonObject.containsKey("timestamp"), "JSON must contain 'timestamp' key")

        val decoded = FoundationJson.decodeFromString(SocketFrame.serializer(), jsonString)
        assertEquals(socketFrame, decoded)
    }

    @Test
    fun foundationJson_ignoresUnknownKeys_onSocketFrameDeserialization() {
        val jsonString = """
            {
                "id": "00000000-0000-0000-0000-000000000002",
                "type": "PING",
                "payload": null,
                "metadata": {},
                "timestamp": 1700000000000,
                "unknown_extra_field": "should_be_ignored"
            }
        """.trimIndent()

        val decoded = FoundationJson.decodeFromString(SocketFrame.serializer(), jsonString)
        assertEquals("00000000-0000-0000-0000-000000000002", decoded.id)
        assertEquals("PING", decoded.type)
        assertNotNull(decoded)
    }
}
