package com.urbantech.smartcity360.interpreter;

import java.util.ArrayList;
import java.util.List;

public final class PolicyChecker {

    private PolicyChecker() {
    }

    public static Evaluation check(String ruleText, CityContext context) {
        try {
            Expression expression = new RuleParser().parse(ruleText);
            boolean result = expression.interpret(context);

            List<String> tree = new ArrayList<>();
            expression.collectTree(tree, 0);
            return Evaluation.success(expression.describe(), tree, context.getTrace(), result);
        } catch (RuleSyntaxException e) {
            return Evaluation.failure(e.getMessage());
        }
    }
}