package io.github.mudrichenkoevgeny.kmp.core.storage.repository.management

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.storagesettings.ManagementStorageSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for management storage settings.
 */
interface ManagementStorageSettingsRepository {
    /**
     * Retrieves management storage settings from cache or network.
     *
     * @return [AppResult.Success] containing [ManagementStorageSettings] or [AppResult.Error].
     */
    suspend fun getManagementStorageSettings(): AppResult<ManagementStorageSettings>

    /**
     * Saves updated management storage settings to remote server and updates local cache.
     *
     * @param storageSettings Target settings to apply.
     * @return [AppResult.Success] containing applied [ManagementStorageSettings] or [AppResult.Error].
     */
    suspend fun saveRemoteManagementStorageSettings(storageSettings: ManagementStorageSettings): AppResult<ManagementStorageSettings>

    /**
     * Resets remote management storage settings to default values and updates local cache.
     *
     * @return [AppResult.Success] containing reset [ManagementStorageSettings] or [AppResult.Error].
     */
    suspend fun resetRemoteManagementStorageSettings(): AppResult<ManagementStorageSettings>

    /**
     * Forces a network reload of management storage settings and updates local cache.
     *
     * @return [AppResult.Success] containing refreshed [ManagementStorageSettings] or [AppResult.Error].
     */
    suspend fun refreshManagementStorageSettings(): AppResult<ManagementStorageSettings>

    /**
     * Updates in-memory and local cache with new storage settings.
     *
     * @param storageSettings Target settings model.
     */
    suspend fun updateManagementStorageSettings(storageSettings: ManagementStorageSettings)

    /**
     * Returns a cold flow emitting management storage settings updates.
     *
     * @return [Flow] emitting [ManagementStorageSettings] or `null`.
     */
    fun observeManagementStorageSettings(): Flow<ManagementStorageSettings?>
}
