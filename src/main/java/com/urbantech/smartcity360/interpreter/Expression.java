package com.urbantech.smartcity360.interpreter;

import java.util.List;

public interface Expression {

    boolean interpret(CityContext context);

    String describe();

    void collectTree(List<String> lines, int depth);
}