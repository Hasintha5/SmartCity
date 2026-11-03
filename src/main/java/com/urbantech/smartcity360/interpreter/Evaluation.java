package com.urbantech.smartcity360.interpreter;

import java.util.ArrayList;
import java.util.List;

public final class Evaluation {

    private final boolean valid;
    private final String error;
    private final String normalized;
    private final List<String> tree;
    private final List<String> trace;
    private final boolean compliant;

    private Evaluation(boolean valid, String error, String normalized,
                       List<String> tree, List<String> trace, boolean compliant) {
        this.valid = valid;
        this.error = error;
        this.normalized = normalized;
        this.tree = tree;
        this.trace = trace;
        this.compliant = compliant;
    }

    static Evaluation success(String normalized, List<String> tree,
                              List<String> trace, boolean compliant) {
        return new Evaluation(true, "", normalized, new ArrayList<>(tree),
                new ArrayList<>(trace), compliant);
    }

    static Evaluation failure(String error) {
        return new Evaluation(false, error, "", new ArrayList<>(), new ArrayList<>(), false);
    }

    public boolean isValid() { return valid; }
    public String getError() { return error; }
    public String getNormalized() { return normalized; }
    public List<String> getTree() { return tree; }
    public List<String> getTrace() { return trace; }
    public boolean isCompliant() { return compliant; }

    public String getTreeText() {
        return String.join("\n", tree);
    }
}