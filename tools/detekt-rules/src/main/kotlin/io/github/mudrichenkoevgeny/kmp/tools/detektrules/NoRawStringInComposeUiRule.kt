package io.github.mudrichenkoevgeny.kmp.tools.detektrules

import dev.detekt.api.Config
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.psiUtil.parentsWithSelf

/**
 * Detekt rule enforcing that raw string literals are not passed directly to Compose UI components
 * (such as `Text`, `Button`, `OutlinedTextField`, `Icon`, `Image`, etc.) in production Composable code.
 */
public class NoRawStringInComposeUiRule(config: Config = Config.empty) : Rule(
    config,
    "Hardcoded string literal in Compose UI element. Use localized strings (e.g., LocalCommonStrings.current) instead."
) {

    private val targetUiFunctions: Set<String> = setOf(
        "Text",
        "Button",
        "OutlinedButton",
        "TextButton",
        "OutlinedTextField",
        "TextField",
        "DropdownMenuItem",
        "Icon",
        "Image"
    )

    private val ignoredArgumentNames: Set<String> = setOf(
        "testTag",
        "key"
    )

    public override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        val calleeName = expression.calleeExpression?.text ?: return
        if (calleeName !in targetUiFunctions) return

        val enclosingFunctions = expression.parentsWithSelf.filterIsInstance<KtNamedFunction>()
        if (enclosingFunctions.any { isPreviewOrTest(it) }) {
            return
        }

        for (argument in expression.valueArguments) {
            val argumentName = argument.getArgumentName()?.text?.trim()
            if (argumentName != null && argumentName in ignoredArgumentNames) {
                continue
            }

            val expr = argument.getArgumentExpression()
            if (expr is KtStringTemplateExpression) {
                report(
                    Finding(
                        entity = Entity.from(argument),
                        message = "Compose UI element '$calleeName' receives a hardcoded string literal. " +
                            "Use localized strings (e.g., LocalCommonStrings.current) instead."
                    )
                )
            }
        }
    }

    private fun isPreviewOrTest(function: KtNamedFunction): Boolean {
        val annotations = function.annotationEntries
        return annotations.any { annotation ->
            val name = annotation.shortName?.asString() ?: ""
            name.contains("Preview") || name == "Test"
        }
    }
}
