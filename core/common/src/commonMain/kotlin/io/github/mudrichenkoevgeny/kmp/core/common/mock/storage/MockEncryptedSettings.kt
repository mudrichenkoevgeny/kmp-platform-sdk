package io.github.mudrichenkoevgeny.kmp.core.common.mock.storage

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MockEncryptedSettings : EncryptedSettings {
    private val mutex = Mutex()
    private val storage = mutableMapOf<String, MutableStateFlow<String?>>()

    override suspend fun put(key: String, value: String) {
        mutex.withLock {
            val flow = storage.getOrPut(key) { MutableStateFlow(null) }
            flow.value = value
        }
    }

    override suspend fun get(key: String): String? {
        return mutex.withLock {
            storage[key]?.value
        }
    }

    override suspend fun remove(key: String) {
        mutex.withLock {
            storage[key]?.value = null
        }
    }

    override fun observe(key: String): Flow<String?> {
        val flow = storage.getOrPut(key) { MutableStateFlow(null) }
        return flow.asStateFlow()
    }
}