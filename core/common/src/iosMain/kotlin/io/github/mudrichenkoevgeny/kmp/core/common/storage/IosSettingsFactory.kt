package io.github.mudrichenkoevgeny.kmp.core.common.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private const val DATASTORE_FILE_NAME = "secure_settings.preferences_pb"

actual fun getSettingsFactory(platformContext: Any?): SettingsFactory = object : SettingsFactory {
    @OptIn(ExperimentalForeignApi::class)
    override fun create(): EncryptedSettings {
        val dataStore = PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = null,
            migrations = emptyList(),
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            produceFile = {
                val directory = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = true,
                    error = null
                )
                val pathString = requireNotNull(directory).path + "/$DATASTORE_FILE_NAME"
                pathString.toPath()
            }
        )
        return DataStoreEncryptedSettings(dataStore)
    }
}

private class DataStoreEncryptedSettings(
    private val dataStore: DataStore<Preferences>
) : EncryptedSettings {
    override suspend fun put(key: String, value: String) {
        dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    override suspend fun get(key: String): String? =
        dataStore.data.first()[stringPreferencesKey(key)]

    override suspend fun remove(key: String) {
        dataStore.edit { it.remove(stringPreferencesKey(key)) }
    }

    override fun observe(key: String): Flow<String?> =
        dataStore.data.map { it[stringPreferencesKey(key)] }
}