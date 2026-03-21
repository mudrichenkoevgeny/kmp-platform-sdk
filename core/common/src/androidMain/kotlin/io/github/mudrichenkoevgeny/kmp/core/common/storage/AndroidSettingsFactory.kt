package io.github.mudrichenkoevgeny.kmp.core.common.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val TINK_KEYSET_NAME = "tink_keyset"
private const val TINK_PREFS_NAME = "tink_prefs"
private const val KEY_TEMPLATE_AES = "AES256_GCM"
private const val MASTER_KEY_URI = "android-keystore://master_key_v1"
private const val DATASTORE_FILE_NAME = "secure_settings"

/**
 * Android [getSettingsFactory]: registers Tink AEAD (`AeadConfig`), builds Android Keystore–backed key material
 * via [AndroidKeysetManager], and returns [EncryptedSettings] backed by Preferences [DataStore] in
 * `secure_settings` under the app data directory.
 *
 * @param platformContext Must be [Context].
 */
actual fun getSettingsFactory(platformContext: Any?): SettingsFactory = object : SettingsFactory {
    override fun create(): EncryptedSettings {
        val context = platformContext as Context

        AeadConfig.register()

        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, TINK_KEYSET_NAME, TINK_PREFS_NAME)
            .withKeyTemplate(KeyTemplates.get(KEY_TEMPLATE_AES))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle

        keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)

        val dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(DATASTORE_FILE_NAME) }
        )

        return DataStoreEncryptedSettings(dataStore)
    }
}

/**
 * [EncryptedSettings] backed by a single [DataStore] of string preferences (values stored under string keys).
 */
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