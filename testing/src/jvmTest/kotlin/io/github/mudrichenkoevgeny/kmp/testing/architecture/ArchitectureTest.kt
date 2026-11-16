package io.github.mudrichenkoevgeny.kmp.testing.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import com.lemonappdev.konsist.api.ext.list.withPackage
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import kotlin.test.Test
import kotlin.test.assertTrue as assertTestTrue

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
        SdkArchitectureAssertions.assertSinglePublicDeclarationPerFile(Konsist.scopeFromProduction())
    }

    @Test
    fun `decompose component interfaces expose state strictly via Value`() {
        SdkArchitectureAssertions.assertDecomposeComponentContracts(Konsist.scopeFromProduction())
    }

    @Test
    fun `DialogConfig and DialogChild declared as sealed class hierarchies in separate files in dialog subpackage`() {
        SdkArchitectureAssertions.assertDialogHierarchyAndSerialization(Konsist.scopeFromProduction())
    }

    @Test
    fun `value class ID contract extension functions`() {
        SdkArchitectureAssertions.assertValueClassIdContracts(Konsist.scopeFromProduction())
    }

    @Test
    fun `forbid direct imports of SharedPreferences DataStore IndexedDB or localStorage in features`() {
        SdkArchitectureAssertions.assertNoRawStorageImportsInFeatures(Konsist.scopeFromProduction())
    }

    @Test
    fun `UI code should not import or use compose stringResource or Res string`() {
        SdkArchitectureAssertions.assertUiLocalization(Konsist.scopeFromProduction())
    }

    @Test
    fun `all public and internal Compose UI components must have complete previews and preview containers`() {
        SdkArchitectureAssertions.assertComposePreviews(Konsist.scopeFromProduction())
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

    @Test
    fun `CoreTheme colors typography dimensions and shapes map to GeneratedDesignTokens`() {
        val themeFiles = Konsist
            .scopeFromProduction()
            .files
            .withPackage("io.github.mudrichenkoevgeny.kmp.core.common.ui.theme")
            .filter { file ->
                val baseName = file.baseFileName()
                baseName == "Color" || baseName == "Typography" || baseName == "CoreDimens" || baseName == "Shapes"
            }

        assertTestTrue(themeFiles.isNotEmpty(), "Theme token mapping files set must not be empty")

        themeFiles.forEach { file ->
            val mapsToTokens = file.text.contains(FILE_GENERATED_DESIGN_TOKENS) ||
                file.imports.any { import -> import.name.endsWith(FILE_GENERATED_DESIGN_TOKENS) }
            assertTestTrue(
                mapsToTokens,
                "Theme file ${file.name} must reference $FILE_GENERATED_DESIGN_TOKENS"
            )
        }
    }

    @Test
    fun `sample modules must not contain core SDK domain repository network API or storage logic`() {
        val sampleFiles = Konsist
            .scopeFromProduction()
            .files
            .filter { file ->
                val pkg = file.packagee?.name ?: ""
                pkg.contains("sampleclient") || pkg.contains("samplemanagement")
            }

        sampleFiles.assertFalse { file ->
            val declarations = file.classes() + file.interfaces() + file.objects()
            declarations.any { decl ->
                decl.name.endsWith("Repository") ||
                    decl.name.endsWith("RepositoryImpl") ||
                    decl.name.endsWith("UseCase") ||
                    decl.name.endsWith("UseCaseImpl") ||
                    decl.name.endsWith("DataSource") ||
                    decl.name.endsWith("StorageImpl") ||
                    decl.name.endsWith("ApiService") ||
                    decl.name.endsWith("ErrorParser")
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
        private const val PKG_SAMPLE_CLIENT = "sampleclient"
        private const val PKG_SAMPLE_MANAGEMENT = "samplemanagement"
        private const val PKG_TESTING = "testing"
        private const val PKG_MOCK = "mock"
        private const val NAME_COMPANION = "Companion"
        private const val SUFFIX_TEST_TAGS = "TestTags"
        private const val FILE_GENERATED_DESIGN_TOKENS = "GeneratedDesignTokens"
    }
}

private fun KoFileDeclaration.baseFileName(): String = name.substringBefore(".")
