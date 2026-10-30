package io.github.mudrichenkoevgeny.kmp.core.storage.uploader

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class KtorDirectFileUploaderTest {

    @Test
    fun uploadFile_returnsSuccess_whenResponseIs200() = runTest {
        var recordedContentLength: Long? = null
        val mockEngine = MockEngine { request ->
            recordedContentLength = request.body.contentLength
            respond("OK", HttpStatusCode.OK)
        }
        val client = HttpClient(mockEngine)
        val uploader = KtorDirectFileUploader(client)

        val testBytes = byteArrayOf(1, 2, 3, 4, 5)
        var progressReported = 0
        val result = uploader.uploadFile(
            uploadUrl = "https://s3.example.com/presigned-upload",
            contentLength = testBytes.size.toLong(),
            openReadChannel = { ByteReadChannel(testBytes) },
            mimeType = "image/png",
            onProgress = { percent -> progressReported = percent }
        )

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(testBytes.size.toLong(), recordedContentLength)
        assertEquals(100, progressReported)
    }

    @Test
    fun uploadFile_returnsError_whenResponseIs500() = runTest {
        val mockEngine = MockEngine {
            respond("Internal Error", HttpStatusCode.InternalServerError)
        }
        val client = HttpClient(mockEngine)
        val uploader = KtorDirectFileUploader(client)

        val result = uploader.uploadFile(
            uploadUrl = "https://s3.example.com/presigned-upload",
            contentLength = 3L,
            openReadChannel = { ByteReadChannel(byteArrayOf(1, 2, 3)) }
        )

        assertIs<AppResult.Error>(result)
    }
}
