package com.urbantech.smartcity360.interpreter;

import java.util.List;

public class ComparisonExpression implements Expression {

    private final CityVariable variable;
    private final Operator operator;
    private final double value;

    public ComparisonExpression(CityVariable variable, Operator operator, double value) {
        this.variable = variable;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean interpret(CityContext context) {
        double actual = context.get(variable);
        boolean result = operator.test(actual, value);
        context.log(variable.getKey() + " is " + CityContext.number(actual)
                + ", check " + describe() + " -> " + result);
        return result;
    }

    @Override
    public String describe() {
        return variable.getKey() + " " + operator.getSymbol() + " " + CityContext.number(value);
    }

    @Override
    public void collectTree(List<String> lines, int depth) {
        lines.add("    ".repeat(depth) + describe());
    }
}