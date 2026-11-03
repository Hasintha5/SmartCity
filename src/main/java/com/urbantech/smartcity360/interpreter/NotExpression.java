package com.urbantech.smartcity360.interpreter;

import java.util.List;

public class NotExpression implements Expression {

    private final Expression inner;

    public NotExpression(Expression inner) {
        this.inner = inner;
    }

    @Override
    public boolean interpret(CityContext context) {
        boolean value = inner.interpret(context);
        context.log("NOT turns " + value + " into " + !value);
        return !value;
    }

    @Override
    public String describe() {
        return "NOT " + inner.describe();
    }

    @Override
    public void collectTree(List<String> lines, int depth) {
        lines.add("    ".repeat(depth) + "NOT");
        inner.collectTree(lines, depth + 1);
    }
}