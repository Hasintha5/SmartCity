package com.urbantech.smartcity360.chain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Complaint {

    private final int id;
    private final String citizen;
    private final String category;
    private final String description;
    private final Severity severity;
    private final List<HandlingStep> steps = new ArrayList<>();
    private String status = "Open";
    private String resolvedBy = "";

    public Complaint(int id, String citizen, String category,
                     String description, Severity severity) {
        this.id = id;
        this.citizen = citizen;
        this.category = category;
        this.description = description;
        this.severity = severity;
    }

    public void addStep(String handler, String action, String note) {
        steps.add(new HandlingStep(handler, action, note));
    }

    public void markResolved(String by) {
        this.status = "Resolved";
        this.resolvedBy = by;
    }

    public void markUnresolved() {
        this.status = "Unresolved";
    }

    public int getId() { return id; }
    public String getCitizen() { return citizen; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public Severity getSeverity() { return severity; }
    public String getStatus() { return status; }
    public String getResolvedBy() { return resolvedBy; }
    public boolean isResolved() { return "Resolved".equals(status); }

    public List<HandlingStep> getSteps() {
        return Collections.unmodifiableList(steps);
    }
}