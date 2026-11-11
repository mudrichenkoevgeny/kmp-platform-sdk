package io.github.mudrichenkoevgeny.kmp.tools.detekt

import dev.detekt.api.RuleSet
import dev.detekt.api.RuleSetId
import dev.detekt.api.RuleSetProvider

/**
 * Detekt rule set provider for KMP platform specific custom static analysis rules.
 */
public class CustomRuleSetProvider : RuleSetProvider {
    /**
     * Unique identifier for the KMP Detekt rule set.
     */
    public override val ruleSetId: RuleSetId = RuleSetId("kmp-rules")

    /**
     * Creates an instance of [RuleSet] containing custom static analysis rules.
     *
     * @return A [RuleSet] instance containing KMP rules.
     */
    public override fun instance(): RuleSet = RuleSet(
        ruleSetId,
        emptyList()
    )
}
