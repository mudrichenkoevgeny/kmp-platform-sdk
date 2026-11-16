package io.github.mudrichenkoevgeny.kmp.testing.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withPackage
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import kotlin.test.Test

class ArchitectureTest {

    @Test
    fun `core-common module should be pure leaf and not depend on feature or other core modules`() {
        Konsist
            .scopeFromProduction()
            .files
            .withPackage(PACKAGE_CORE_COMMON)
            .assertFalse { file ->
                file.imports.any { import ->
                    val name = import.name
                    name.startsWith(PREFIX_FEATURE) ||
                        (
                            name.startsWith(PREFIX_CORE) &&
                                !name.startsWith(PREFIX_CORE_COMMON)
                            )
                }
            }
    }

    @Test
    fun `feature modules should only depend on core infrastructure and allowed dependencies`() {
        Konsist
            .scopeFromProduction()
            .files
            .withPackage(PACKAGE_FEATURE)
            .assertFalse { file ->
                val currentFeature = file.packagee?.name?.substringAfter(PREFIX_FEATURE)?.substringBefore(".")

                file.imports.any { import ->
                    val name = import.name
                    name.startsWith(PREFIX_FEATURE) &&
                        currentFeature != null &&
                        !name.startsWith("$PREFIX_FEATURE$currentFeature.") &&
                        !name.startsWith("$PREFIX_FEATURE$FEATURE_USER.")
                }
            }
    }

    @Test
    fun `every public interface, class, or object should reside in its own file matching the declaration name`() {
        Konsist.scopeFromProduction().classes(includeNested = false).assertTrue {
            if (it.hasPublicModifier) {
                it.containingFile.baseFileName() == it.name
            } else {
                true
            }
        }

        Konsist.scopeFromProduction().interfaces(includeNested = false).assertTrue {
            if (it.hasPublicModifier) {
                it.containingFile.baseFileName() == it.name
            } else {
                true
            }
        }

        Konsist.scopeFromProduction().objects(includeNested = false).assertTrue {
            if (it.hasPublicModifier && it.name != NAME_COMPANION && !it.name.endsWith(SUFFIX_TEST_TAGS)) {
                val baseName = it.containingFile.baseFileName()
                baseName == it.name ||
                    it.name == "$baseName$SUFFIX_CONFIG" ||
                    it.name == "$baseName$SUFFIX_DEFAULTS"
            } else {
                true
            }
        }
    }

    @Test
    fun `no file should contain multiple public top-level declarations`() {
        Konsist.scopeFromProduction().files.assertTrue { file ->
            val baseName = file.baseFileName()
            val publicTopLevelDeclarations = (
                file.classes(includeNested = false) +
                    file.interfaces(includeNested = false) +
                    file.objects(includeNested = false).filterNot {
                        it.name.endsWith(SUFFIX_TEST_TAGS) ||
                            it.name == "$baseName$SUFFIX_CONFIG" ||
                            it.name == "$baseName$SUFFIX_DEFAULTS"
                    }
                ).filter { it.hasPublicModifier }

            publicTopLevelDeclarations.size <= 1
        }
    }

    @Test
    fun `decompose component interfaces expose state strictly via Value`() {
        Konsist
            .scopeFromProduction()
            .interfaces()
            .withNameEndingWith(SUFFIX_COMPONENT)
            .assertTrue { component ->
                component.properties().all { prop ->
                    if (prop.name == PROP_STATE || prop.name.endsWith(SUFFIX_STATE)) {
                        prop.type?.text?.startsWith(TYPE_VALUE_PREFIX) == true
                    } else if (prop.name == PROP_DIALOG_SLOT || prop.name.endsWith(SUFFIX_SLOT)) {
                        prop.type?.text?.startsWith(TYPE_CHILD_SLOT_PREFIX) == true
                    } else {
                        true
                    }
                }
            }
    }

    @Test
    fun `forbid mutable properties on public component interfaces`() {
        Konsist
            .scopeFromProduction()
            .interfaces()
            .withNameEndingWith(SUFFIX_COMPONENT)
            .assertTrue { component ->
                component.properties().none { prop -> prop.isVar }
            }
    }

    @Test
    fun `DialogConfig and DialogChild declared as sealed class hierarchies in separate files in dialog subpackage`() {
        val scope = Konsist.scopeFromProduction()
        val declarations = scope.classes().withNameEndingWith(SUFFIX_DIALOG_CONFIG, SUFFIX_DIALOG_CHILD) +
            scope.interfaces().withNameEndingWith(SUFFIX_DIALOG_CONFIG, SUFFIX_DIALOG_CHILD)

        declarations.assertTrue {
            val pkg = it.packagee?.name
            val isInDialog = pkg != null &&
                (pkg == PKG_DIALOG || pkg.endsWith(PKG_DIALOG) || pkg.contains(PKG_DIALOG_DOT))
            isInDialog &&
                it.hasSealedModifier &&
                it.containingFile.baseFileName() == it.name
        }
    }

    @Test
    fun `DialogConfig classes annotated with Serializable`() {
        val scope = Konsist.scopeFromProduction()
        val declarations = scope.classes().withNameEndingWith(SUFFIX_DIALOG_CONFIG) +
            scope.interfaces().withNameEndingWith(SUFFIX_DIALOG_CONFIG)

        declarations.assertTrue {
            it.hasAnnotationWithName(ANNOTATION_SERIALIZABLE)
        }
    }

    @Test
    fun `value class ID contract extension functions`() {
        Konsist
            .scopeFromProduction()
            .classes()
            .withNameEndingWith(SUFFIX_ID)
            .assertTrue {
                if (it.hasValueModifier) {
                    val file = it.containingFile
                    val hasAsHex = file.functions().any { func -> func.name == FUNC_AS_HEX_DASH_STRING }
                    val hasToOrNull = file.functions().any { func -> func.name == "to${it.name}OrNull" }
                    val hasToOrThrow = file.functions().any { func -> func.name == "to${it.name}OrThrow" }

                    hasAsHex && hasToOrNull && hasToOrThrow
                } else {
                    true
                }
            }
    }

    @Test
    fun `forbid direct imports of SharedPreferences DataStore IndexedDB or localStorage in features`() {
        Konsist
            .scopeFromProduction()
            .files
            .withPackage(PACKAGE_FEATURE)
            .assertFalse { file ->
                file.imports.any { import ->
                    val name = import.name
                    name.startsWith(IMPORT_SHARED_PREFERENCES) ||
                        name.startsWith(IMPORT_DATASTORE_PREFIX) ||
                        name.contains(KEYWORD_INDEXED_DB) ||
                        name.contains(KEYWORD_LOCAL_STORAGE)
                }
            }
    }

    @Test
    fun `enforce structured KDoc on public API surface`() {
        val scope = Konsist.scopeFromProduction()

        val isExcludedPkg: (String?) -> Boolean = { pkg ->
            val p = pkg ?: ""
            p.contains(PKG_SAMPLE_CLIENT) ||
                p.contains(PKG_SAMPLE_MANAGEMENT) ||
                p.contains(PKG_TESTING) ||
                p.contains(PKG_MOCK)
        }

        val isExcludedFile: (KoFileDeclaration) -> Boolean = { file ->
            file.name.contains(FILE_GENERATED_DESIGN_TOKENS)
        }

        scope.classes(includeNested = true).assertTrue {
            if (it.hasPublicModifier &&
                !isExcludedPkg(it.containingFile.packagee?.name) &&
                !isExcludedFile(it.containingFile)
            ) {
                it.hasKDoc
            } else {
                true
            }
        }

        scope.interfaces(includeNested = true).assertTrue {
            if (it.hasPublicModifier &&
                !isExcludedPkg(it.containingFile.packagee?.name) &&
                !isExcludedFile(it.containingFile)
            ) {
                it.hasKDoc
            } else {
                true
            }
        }

        scope.objects(includeNested = true).assertTrue {
            if (it.hasPublicModifier &&
                !isExcludedPkg(it.containingFile.packagee?.name) &&
                !isExcludedFile(it.containingFile) &&
                it.name != NAME_COMPANION &&
                !it.name.endsWith(SUFFIX_TEST_TAGS)
            ) {
                it.hasKDoc
            } else {
                true
            }
        }

        scope.functions(includeNested = true).assertTrue {
            if (it.hasPublicModifier &&
                !it.hasOverrideModifier &&
                !isExcludedPkg(it.containingFile.packagee?.name) &&
                !isExcludedFile(it.containingFile)
            ) {
                it.hasKDoc
            } else {
                true
            }
        }
    }

    private companion object {
        private const val PACKAGE_CORE_COMMON = "io.github.mudrichenkoevgeny.kmp.core.common.."
        private const val PACKAGE_FEATURE = "io.github.mudrichenkoevgeny.kmp.feature.."
        private const val PREFIX_FEATURE = "io.github.mudrichenkoevgeny.kmp.feature."
        private const val PREFIX_CORE = "io.github.mudrichenkoevgeny.kmp.core."
        private const val PREFIX_CORE_COMMON = "io.github.mudrichenkoevgeny.kmp.core.common."
        private const val FEATURE_USER = "user"
        private const val SUFFIX_COMPONENT = "Component"
        private const val SUFFIX_CONFIG = "Config"
        private const val SUFFIX_DEFAULTS = "Defaults"
        private const val SUFFIX_DIALOG_CONFIG = "DialogConfig"
        private const val SUFFIX_DIALOG_CHILD = "DialogChild"
        private const val SUFFIX_ID = "Id"
        private const val SUFFIX_TEST_TAGS = "TestTags"
        private const val NAME_COMPANION = "Companion"
        private const val ANNOTATION_SERIALIZABLE = "Serializable"
        private const val FUNC_AS_HEX_DASH_STRING = "asHexDashString"
        private const val PROP_STATE = "state"
        private const val SUFFIX_STATE = "State"
        private const val PROP_DIALOG_SLOT = "dialogSlot"
        private const val SUFFIX_SLOT = "Slot"
        private const val TYPE_VALUE_PREFIX = "Value<"
        private const val TYPE_CHILD_SLOT_PREFIX = "Value<ChildSlot<"
        private const val PKG_DIALOG = "dialog"
        private const val PKG_DIALOG_DOT = ".dialog."
        private const val IMPORT_SHARED_PREFERENCES = "android.content.SharedPreferences"
        private const val IMPORT_DATASTORE_PREFIX = "androidx.datastore."
        private const val KEYWORD_INDEXED_DB = "IndexedDB"
        private const val KEYWORD_LOCAL_STORAGE = "localStorage"
        private const val PKG_SAMPLE_CLIENT = "sampleclient"
        private const val PKG_SAMPLE_MANAGEMENT = "samplemanagement"
        private const val PKG_TESTING = "testing"
        private const val PKG_MOCK = "mock"
        private const val FILE_GENERATED_DESIGN_TOKENS = "GeneratedDesignTokens"
    }
}

private fun KoFileDeclaration.baseFileName(): String = name.substringBefore(".")
