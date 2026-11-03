package com.urbantech.smartcity360.interpreter;

public enum Operator {

    LESS("<"),
    LESS_OR_EQUAL("<="),
    GREATER(">"),
    GREATER_OR_EQUAL(">="),
    EQUAL("=="),
    NOT_EQUAL("!=");

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean test(double left, double right) {
        return switch (this) {
            case LESS -> left < right;
            case LESS_OR_EQUAL -> left <= right;
            case GREATER -> left > right;
            case GREATER_OR_EQUAL -> left >= right;
            case EQUAL -> Math.abs(left - right) < 1e-9;
            case NOT_EQUAL -> Math.abs(left - right) >= 1e-9;
        };
    }

    public static Operator fromSymbol(String symbol) {
        for (Operator operator : values()) {
            if (operator.symbol.equals(symbol)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unknown operator: " + symbol);
    }
}