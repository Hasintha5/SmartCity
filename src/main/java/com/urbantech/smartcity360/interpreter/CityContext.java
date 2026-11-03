package com.urbantech.smartcity360.interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CityContext {

    private final Map<CityVariable, Double> values = new EnumMap<>(CityVariable.class);
    private final List<String> trace = new ArrayList<>();

    public CityContext() {
        for (CityVariable variable : CityVariable.values()) {
            values.put(variable, variable.getDefaultValue());
        }
    }

    public CityContext set(CityVariable variable, double value) {
        values.put(variable, value);
        return this;
    }

    public CityContext setAll(Map<CityVariable, Double> readings) {
        values.putAll(readings);
        return this;
    }

    public double get(CityVariable variable) {
        return values.get(variable);
    }

    public void log(String line) {
        trace.add(line);
    }

    public List<String> getTrace() {
        return Collections.unmodifiableList(trace);
    }

    public static String number(double value) {
        if (value == Math.rint(value) && Math.abs(value) < 1e15) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }
}