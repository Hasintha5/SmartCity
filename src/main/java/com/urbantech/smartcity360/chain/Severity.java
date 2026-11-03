package com.urbantech.smartcity360.chain;

public enum Severity {

    LOW("Low", "Small problem. Local staff can fix it.", 1),
    MEDIUM("Medium", "Needs a supervisor to decide.", 2),
    HIGH("High", "Serious. Needs the city manager.", 3);

    private final String label;
    private final String hint;
    private final int rank;

    Severity(String label, String hint, int rank) {
        this.label = label;
        this.hint = hint;
        this.rank = rank;
    }

    public String getLabel() { return label; }
    public String getHint() { return hint; }
    public int getRank() { return rank; }
    public String getCode() { return name(); }

    public static Severity fromParam(String text) {
        for (Severity s : values()) {
            if (s.name().equalsIgnoreCase(text)) {
                return s;
            }
        }
        return LOW;
    }
}