package com.urbantech.smartcity360.interpreter;

import java.util.List;

public class OrExpression implements Expression {

    private final Expression left;
    private final Expression right;

    public OrExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(CityContext context) {
        if (left.interpret(context)) {
            context.log("OR: left side is true, right side not checked");
            return true;
        }
        return right.interpret(context);
    }

    @Override
    public String describe() {
        return "(" + left.describe() + " OR " + right.describe() + ")";
    }

    @Override
    public void collectTree(List<String> lines, int depth) {
        lines.add("    ".repeat(depth) + "OR");
        left.collectTree(lines, depth + 1);
        right.collectTree(lines, depth + 1);
    }
}