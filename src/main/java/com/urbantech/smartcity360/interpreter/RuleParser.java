package com.urbantech.smartcity360.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RuleParser {

    private enum Type { NUMBER, WORD, AND, OR, NOT, LEFT, RIGHT, OPERATOR, END }

    private static final class Token {
        private final Type type;
        private final String text;
        private final int position;

        private Token(Type type, String text, int position) {
            this.type = type;
            this.text = text;
            this.position = position;
        }
    }

    private List<Token> tokens = new ArrayList<>();
    private int index;

    public Expression parse(String text) {
        tokens = tokenize(text == null ? "" : text);
        index = 0;

        if (peek().type == Type.END) {
            throw new RuleSyntaxException("The rule is empty. Write something like: noise <= 70");
        }

        Expression result = parseOr();

        Token extra = peek();
        if (extra.type == Type.RIGHT) {
            throw new RuleSyntaxException("The closing bracket ) at position " + (extra.position + 1)
                    + " has no matching opening bracket.");
        }
        if (extra.type != Type.END) {
            throw new RuleSyntaxException("Unexpected " + quote(extra) + " at position "
                    + (extra.position + 1) + ". Join two conditions with AND or OR.");
        }
        return result;
    }


    private Expression parseOr() {
        Expression left = parseAnd();
        while (peek().type == Type.OR) {
            next();
            left = new OrExpression(left, parseAnd());
        }
        return left;
    }

    private Expression parseAnd() {
        Expression left = parseNot();
        while (peek().type == Type.AND) {
            next();
            left = new AndExpression(left, parseNot());
        }
        return left;
    }

    private Expression parseNot() {
        if (peek().type == Type.NOT) {
            next();
            return new NotExpression(parseNot());
        }
        return parsePrimary();
    }

    private Expression parsePrimary() {
        if (peek().type == Type.LEFT) {
            Token open = next();
            Expression inner = parseOr();
            if (peek().type != Type.RIGHT) {
                throw new RuleSyntaxException("The bracket opened at position " + (open.position + 1)
                        + " is never closed. Add a closing bracket ).");
            }
            next();
            return inner;
        }
        return parseComparison();
    }

    private Expression parseComparison() {
        Token word = next();
        if (word.type != Type.WORD) {
            throw new RuleSyntaxException("Expected a variable name at position " + (word.position + 1)
                    + " but found " + quote(word) + ". Variables: " + CityVariable.allKeys() + ".");
        }
        CityVariable variable = CityVariable.fromKey(word.text);
        if (variable == null) {
            throw new RuleSyntaxException("Unknown variable '" + word.text + "' at position "
                    + (word.position + 1) + ". Variables: " + CityVariable.allKeys() + ".");
        }

        Token operator = next();
        if (operator.type != Type.OPERATOR) {
            throw new RuleSyntaxException("Expected a comparison (<, <=, >, >=, ==, !=) after '"
                    + word.text + "' at position " + (operator.position + 1)
                    + " but found " + quote(operator) + ".");
        }

        Token number = next();
        if (number.type != Type.NUMBER) {
            throw new RuleSyntaxException("Expected a number after '" + operator.text + "' at position "
                    + (number.position + 1) + " but found " + quote(number) + ".");
        }

        return new ComparisonExpression(variable, Operator.fromSymbol(operator.text),
                Double.parseDouble(number.text));
    }


    private Token peek() {
        return tokens.get(index);
    }

    private Token next() {
        Token token = tokens.get(index);
        if (token.type != Type.END) {
            index++;
        }
        return token;
    }

    private static String quote(Token token) {
        return token.type == Type.END ? "the end of the rule" : "'" + token.text + "'";
    }


    private List<Token> tokenize(String text) {
        List<Token> list = new ArrayList<>();
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
            } else if (c == '(') {
                list.add(new Token(Type.LEFT, "(", i));
                i++;
            } else if (c == ')') {
                list.add(new Token(Type.RIGHT, ")", i));
                i++;
            } else if (c == '<' || c == '>' || c == '=' || c == '!') {
                boolean hasEquals = i + 1 < text.length() && text.charAt(i + 1) == '=';
                if (hasEquals) {
                    list.add(new Token(Type.OPERATOR, c + "=", i));
                    i += 2;
                } else if (c == '<' || c == '>') {
                    list.add(new Token(Type.OPERATOR, String.valueOf(c), i));
                    i++;
                } else {
                    throw new RuleSyntaxException("Unexpected '" + c + "' at position " + (i + 1) + ". "
                            + (c == '=' ? "Use == to compare." : "Use != for not equal."));
                }
            } else if (Character.isDigit(c)
                    || (c == '-' && i + 1 < text.length() && Character.isDigit(text.charAt(i + 1)))) {
                int start = i;
                i++;
                while (i < text.length()
                        && (Character.isDigit(text.charAt(i)) || text.charAt(i) == '.')) {
                    i++;
                }
                String number = text.substring(start, i);
                try {
                    Double.parseDouble(number);
                } catch (NumberFormatException e) {
                    throw new RuleSyntaxException("'" + number + "' at position " + (start + 1)
                            + " is not a valid number.");
                }
                list.add(new Token(Type.NUMBER, number, start));
            } else if (Character.isLetter(c) || c == '_') {
                int start = i;
                while (i < text.length()
                        && (Character.isLetterOrDigit(text.charAt(i)) || text.charAt(i) == '_')) {
                    i++;
                }
                String word = text.substring(start, i);
                String upper = word.toUpperCase(Locale.ROOT);
                if (upper.equals("AND")) {
                    list.add(new Token(Type.AND, upper, start));
                } else if (upper.equals("OR")) {
                    list.add(new Token(Type.OR, upper, start));
                } else if (upper.equals("NOT")) {
                    list.add(new Token(Type.NOT, upper, start));
                } else {
                    list.add(new Token(Type.WORD, word.toLowerCase(Locale.ROOT), start));
                }
            } else {
                throw new RuleSyntaxException("Unexpected character '" + c + "' at position " + (i + 1) + ".");
            }
        }
        list.add(new Token(Type.END, "", text.length()));
        return list;
    }
}