package com.urbantech.smartcity360.interpreter;

import java.util.List;

public class AndExpression implements Expression {

    private final Expression left;
    private final Expression right;

    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(CityContext context) {
        if (!left.interpret(context)) {
            context.log("AND: left side is false, right side not checked");
            return false;
        }
        return right.interpret(context);
    }

    @Override
    public String describe() {
        return "(" + left.describe() + " AND " + right.describe() + ")";
    }

    @Override
    public void collectTree(List<String> lines, int depth) {
        lines.add("    ".repeat(depth) + "AND");
        left.collectTree(lines, depth + 1);
        right.collectTree(lines, depth + 1);
    }
}