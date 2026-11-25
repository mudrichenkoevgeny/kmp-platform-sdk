package io.github.mudrichenkoevgeny.kmp.tools.detektrules

import dev.detekt.api.Config
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import org.jetbrains.kotlin.psi.KtClass

/**
 * Detekt rule enforcing that Robolectric screenshot test classes declare
 * `@GraphicsMode(GraphicsMode.Mode.NATIVE)` to ensure deterministic rendering across OS environments.
 */
public class ScreenshotTestGraphicsModeRule(config: Config = Config.empty) : Rule(
    config,
    "Screenshot test classes must be annotated with @GraphicsMode(GraphicsMode.Mode.NATIVE)."
) {

    public override fun visitClass(klass: KtClass) {
        super.visitClass(klass)

        val className = klass.name ?: return
        if (!className.endsWith("ScreenshotTest")) {
            return
        }

        val hasNativeGraphicsMode = klass.annotationEntries.any { annotation ->
            val shortName = annotation.shortName?.asString()
            if (shortName != "GraphicsMode") {
                return@any false
            }
            annotation.valueArguments.any { argument ->
                val text = argument.getArgumentExpression()?.text ?: ""
                text == "GraphicsMode.Mode.NATIVE" || text == "Mode.NATIVE" || text == "NATIVE"
            }
        }

        if (!hasNativeGraphicsMode) {
            report(
                Finding(
                    entity = Entity.from(klass),
                    message = "Screenshot test '$className' must be annotated with " +
                        "@GraphicsMode(GraphicsMode.Mode.NATIVE)."
                )
            )
        }
    }
}
