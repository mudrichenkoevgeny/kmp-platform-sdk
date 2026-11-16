package io.github.mudrichenkoevgeny.kmp.testing.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.container.KoScope
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import com.lemonappdev.konsist.api.declaration.KoFunctionDeclaration
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withPackage
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue

/**
 * Reusable Konsist assertions for validating architecture and coding standards
 * across KMP platform SDK consumer projects.
 */
public object SdkArchitectureAssertions {

    /**
     * Asserts that every public interface, class, or object resides in its own dedicated
     * file matching the declaration name, and no file contains multiple public top-level declarations.
     *
     * @param scope The [KoScope] to analyze.
     */
    public fun assertSinglePublicDeclarationPerFile(scope: KoScope = Konsist.scopeFromProduction()) {
        scope.classes(includeNested = false).assertTrue(
            testName = "assertSinglePublicDeclarationPerFile - classes"
        ) {
            if (it.hasPublicModifier) {
                it.containingFile.baseFileName() == it.name
            } else {
                true
            }
        }

        scope.interfaces(includeNested = false).assertTrue(
            testName = "assertSinglePublicDeclarationPerFile - interfaces"
        ) {
            if (it.hasPublicModifier) {
                it.containingFile.baseFileName() == it.name
            } else {
                true
            }
        }

        scope.objects(includeNested = false).assertTrue(
            testName = "assertSinglePublicDeclarationPerFile - objects"
        ) {
            if (it.hasPublicModifier && it.name != "Companion" && !it.name.endsWith("TestTags")) {
                val baseName = it.containingFile.baseFileName()
                baseName == it.name ||
                    it.name == "${baseName}Config" ||
                    it.name == "${baseName}Defaults"
            } else {
                true
            }
        }

        scope.files.assertTrue(
            testName = "assertSinglePublicDeclarationPerFile - multiple top-level declarations"
        ) { file ->
            val baseName = file.baseFileName()
            val publicTopLevelDeclarations = (
                file.classes(includeNested = false) +
                    file.interfaces(includeNested = false) +
                    file.objects(includeNested = false).filterNot {
                        it.name.endsWith("TestTags") ||
                            it.name == "${baseName}Config" ||
                            it.name == "${baseName}Defaults"
                    }
                ).filter { it.hasPublicModifier }

            publicTopLevelDeclarations.size <= 1
        }
    }

    /**
     * Asserts that Decompose component interfaces expose state via `Value<T>`
     * and forbid mutable properties.
     *
     * @param scope The [KoScope] to analyze.
     */
    public fun assertDecomposeComponentContracts(scope: KoScope = Konsist.scopeFromProduction()) {
        val components = scope.interfaces().withNameEndingWith("Component")

        components.assertTrue(testName = "assertDecomposeComponentContracts - expose state via Value") { component ->
            component.properties().all { prop ->
                if (prop.name == "state" || prop.name.endsWith("State")) {
                    prop.type?.text?.startsWith("Value<") == true
                } else if (prop.name == "dialogSlot" || prop.name.endsWith("Slot")) {
                    prop.type?.text?.startsWith("Value<ChildSlot<") == true
                } else {
                    true
                }
            }
        }

        components.assertTrue(
            testName = "assertDecomposeComponentContracts - forbid mutable properties"
        ) { component ->
            component.properties().none { prop -> prop.isVar }
        }
    }

    /**
     * Asserts that Decompose Dialog configurations are sealed classes in a dialog subpackage
     * and annotated with `@Serializable`.
     *
     * @param scope The [KoScope] to analyze.
     */
    public fun assertDialogHierarchyAndSerialization(scope: KoScope = Konsist.scopeFromProduction()) {
        val declarations = scope.classes().withNameEndingWith("DialogConfig", "DialogChild") +
            scope.interfaces().withNameEndingWith("DialogConfig", "DialogChild")

        declarations.assertTrue(testName = "assertDialogHierarchyAndSerialization - hierarchy") {
            val pkg = it.packagee?.name
            val isInDialog = pkg != null &&
                (pkg == "dialog" || pkg.endsWith("dialog") || pkg.contains(".dialog."))
            isInDialog &&
                it.hasSealedModifier &&
                it.containingFile.baseFileName() == it.name
        }

        val configs = scope.classes().withNameEndingWith("DialogConfig") +
            scope.interfaces().withNameEndingWith("DialogConfig")

        configs.assertTrue(testName = "assertDialogHierarchyAndSerialization - Serializable") {
            it.hasAnnotationWithName("Serializable")
        }
    }

    /**
     * Asserts that domain ID value classes have required conversion extension functions.
     *
     * @param scope The [KoScope] to analyze.
     */
    public fun assertValueClassIdContracts(scope: KoScope = Konsist.scopeFromProduction()) {
        scope.classes().withNameEndingWith("Id").assertTrue(testName = "assertValueClassIdContracts") {
            if (it.hasValueModifier) {
                val file = it.containingFile
                val hasAsHex = file.functions().any { func -> func.name == "asHexDashString" }
                val hasToOrNull = file.functions().any { func -> func.name == "to${it.name}OrNull" }
                val hasToOrThrow = file.functions().any { func -> func.name == "to${it.name}OrThrow" }

                hasAsHex && hasToOrNull && hasToOrThrow
            } else {
                true
            }
        }
    }

    /**
     * Asserts that raw storage imports (SharedPreferences, DataStore, IndexedDB, localStorage)
     * are forbidden in feature packages.
     *
     * @param scope The [KoScope] to analyze.
     * @param featurePackage Package pattern for features.
     */
    public fun assertNoRawStorageImportsInFeatures(
        scope: KoScope = Konsist.scopeFromProduction(),
        featurePackage: String = "..feature.."
    ) {
        scope.files.withPackage(featurePackage).assertFalse(
            testName = "assertNoRawStorageImportsInFeatures"
        ) { file ->
            file.imports.any { import ->
                val name = import.name
                name.startsWith("android.content.SharedPreferences") ||
                    name.startsWith("androidx.datastore.") ||
                    name.contains("IndexedDB") ||
                    name.contains("localStorage")
            }
        }
    }

    /**
     * Asserts that compose `stringResource`, `getString`, and `Res.string` are not used in UI packages.
     *
     * @param scope The [KoScope] to analyze.
     * @param uiPackage Package pattern for UI components.
     * @param excludedFiles Optional filenames to exclude from the check.
     */
    public fun assertUiLocalization(
        scope: KoScope = Konsist.scopeFromProduction(),
        uiPackage: String = "..ui..",
        excludedFiles: Set<String> = emptySet()
    ) {
        scope.files.withPackage(uiPackage).assertFalse(testName = "assertUiLocalization") { file ->
            if (excludedFiles.any { file.name.contains(it) }) {
                false
            } else {
                val hasForbiddenImport = file.imports.any { import ->
                    import.name == "org.jetbrains.compose.resources.stringResource" ||
                        import.name == "org.jetbrains.compose.resources.getString"
                }
                val hasResStringInText = file.text.contains("Res.string") ||
                    file.text.contains(".Res.string.")

                hasForbiddenImport || hasResStringInText
            }
        }
    }

    /**
     * Asserts that public and internal UI components have complete Compose previews and use standard preview containers.
     *
     * @param scope The [KoScope] to analyze.
     * @param uiPackage Package pattern for UI components.
     * @param excludedPackages Sub-packages to ignore.
     * @param excludedFunctionNames Specific function names to ignore.
     */
    public fun assertComposePreviews(
        scope: KoScope = Konsist.scopeFromProduction(),
        uiPackage: String = "..ui..",
        excludedPackages: Set<String> = setOf(
            ".ui.preview",
            ".ui.test",
            ".ui.theme",
            ".ui.component.scrollbar"
        ),
        excludedFunctionNames: Set<String> = setOf("OnBottomReach", "CoreTheme")
    ) {
        scope.functions().withPackage(uiPackage).assertTrue(
            testName = "assertComposePreviews"
        ) { function ->
            if (isTargetUiFunction(function, excludedPackages, excludedFunctionNames)) {
                validateFunctionPreviews(function)
            } else {
                true
            }
        }
    }

    /**
     * Executes all consumer-facing architectural tests in one call.
     *
     * @param scope The [KoScope] to analyze. Defaults to production code scope.
     * @param featurePackage Package pattern for features. Defaults to "..feature..".
     * @param uiPackage Package pattern for UI components. Defaults to "..ui..".
     * @param localizationExcludedFiles Files excluded from `stringResource` validation.
     * @param previewExcludedPackages Packages excluded from preview validation.
     * @param previewExcludedFunctionNames Functions excluded from preview validation.
     */
    public fun assertAllConsumerRules(
        scope: KoScope = Konsist.scopeFromProduction(),
        featurePackage: String = "..feature..",
        uiPackage: String = "..ui..",
        localizationExcludedFiles: Set<String> = emptySet(),
        previewExcludedPackages: Set<String> = setOf(
            ".ui.preview",
            ".ui.test",
            ".ui.theme",
            ".ui.component.scrollbar"
        ),
        previewExcludedFunctionNames: Set<String> = setOf("OnBottomReach", "CoreTheme")
    ) {
        assertSinglePublicDeclarationPerFile(scope)
        assertDecomposeComponentContracts(scope)
        assertDialogHierarchyAndSerialization(scope)
        assertValueClassIdContracts(scope)
        assertNoRawStorageImportsInFeatures(scope, featurePackage)
        assertUiLocalization(scope, uiPackage, localizationExcludedFiles)
        assertComposePreviews(scope, uiPackage, previewExcludedPackages, previewExcludedFunctionNames)
    }

    private fun isTargetUiFunction(
        function: KoFunctionDeclaration,
        excludedPackages: Set<String>,
        excludedFunctionNames: Set<String>
    ): Boolean {
        val pkgName = function.containingFile.packagee?.name ?: ""
        if (excludedPackages.any { pkgName.contains(it) }) return false
        if (function.name in excludedFunctionNames) return false

        val isComposable = function.hasAnnotationWithName("Composable")
        val isNotPreview = function.annotations.none { it.name.startsWith("Preview") }
        val isCapitalized = function.name.firstOrNull()?.isUpperCase() == true
        val isPublicOrInternal = function.hasPublicModifier || function.hasInternalModifier

        return isComposable && isNotPreview && isCapitalized && isPublicOrInternal
    }

    private fun validateFunctionPreviews(function: KoFunctionDeclaration): Boolean {
        val file = function.containingFile
        val previewFunctions = file.functions().filter { f ->
            f.annotations.any { it.name.startsWith("Preview") }
        }

        if (previewFunctions.isEmpty()) return false

        val hasSizePreview = previewFunctions.any { f -> f.annotations.any { it.name.endsWith("Sizes") } }
        val hasThemePreview = previewFunctions.any { f -> f.annotations.any { it.name == "PreviewThemes" } }
        val allPreviewsUseContainer = previewFunctions.all { f -> hasPreviewContainer(f.text) }

        val rendersText = rendersText(file.text)
        val hasFontScalePreview = if (rendersText) {
            previewFunctions.any { f -> f.annotations.any { it.name == "PreviewFontScales" } }
        } else {
            true
        }

        return hasSizePreview && hasThemePreview && hasFontScalePreview && allPreviewsUseContainer
    }

    private fun hasPreviewContainer(bodyText: String): Boolean =
        bodyText.contains("ComponentPreviewContainer") ||
            bodyText.contains("ScreenPreviewContainer") ||
            bodyText.contains("DialogPreviewContainer")

    private fun rendersText(fileText: String): Boolean =
        fileText.contains("Text(") ||
            fileText.contains("BasicText") ||
            fileText.contains("TextField") ||
            fileText.contains("OutlinedTextField") ||
            fileText.contains("ScreenPreviewContainer") ||
            fileText.contains("DialogPreviewContainer")

    private fun KoFileDeclaration.baseFileName(): String = name.substringBefore(".")
}
