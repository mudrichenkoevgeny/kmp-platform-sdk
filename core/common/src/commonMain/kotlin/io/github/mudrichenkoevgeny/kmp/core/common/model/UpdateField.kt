package io.github.mudrichenkoevgeny.kmp.core.common.model

import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.patch.PatchField

/**
 * todo refactoring, mb move to shared lib
 * Represents a tri-state update operation for domain models.
 *
 * Used across use cases and repositories to distinguish between omitting an update
 * and explicitly setting a field to a new value or `null`.
 *
 * @param T The type of the wrapped value.
 */
sealed class UpdateField<out T> {

    /**
     * Indicates that the field should be ignored during update operations.
     */
    data object Ignore : UpdateField<Nothing>()

    /**
     * Indicates that the field must be updated to the provided [value].
     *
     * @property value The target value to set, or `null` to clear the field.
     */
    data class Set<T>(val value: T?) : UpdateField<T>()
}

/**
 * Executes [block] with the target value only when this [UpdateField] is [UpdateField.Set].
 *
 * @param T The type of the wrapped value.
 * @param block The lambda invoked with the nullable target value.
 */
fun <T> UpdateField<T>.onSet(block: (T?) -> Unit) {
    if (this is UpdateField.Set) {
        block(value)
    }
}

/**
 * Converts a domain-layer [UpdateField] into a network-layer [PatchField].
 *
 * @param T The type of the wrapped value.
 * @return [PatchField.Unchanged] if ignored, or [PatchField.Value] wrapping the target value.
 */
fun <T> UpdateField<T>.toPatchField(): PatchField<T> = when (this) {
    is UpdateField.Ignore -> PatchField.Unchanged
    is UpdateField.Set -> PatchField.Value(value)
}

/**
 * Converts a domain-layer [UpdateField] into a network-layer [PatchField],
 * transforming the non-null value using [transform].
 *
 * @param T The source domain value type.
 * @param R The target network value type.
 * @param transform Mapping function applied to non-null values.
 * @return [PatchField.Unchanged] if ignored, or [PatchField.Value] wrapping the transformed value or `null`.
 */
fun <T, R> UpdateField<T>.toPatchField(transform: (T) -> R?): PatchField<R> = when (this) {
    is UpdateField.Ignore -> PatchField.Unchanged
    is UpdateField.Set -> PatchField.Value(value?.let(transform))
}

/**
 * Converts a network-layer [PatchField] into a domain-layer [UpdateField].
 *
 * @param T The type of the wrapped value.
 * @return [UpdateField.Ignore] if unchanged, or [UpdateField.Set] wrapping the target value.
 */
fun <T> PatchField<T>.toUpdateField(): UpdateField<T> = when (this) {
    is PatchField.Unchanged -> UpdateField.Ignore
    is PatchField.Value -> UpdateField.Set(value)
}
