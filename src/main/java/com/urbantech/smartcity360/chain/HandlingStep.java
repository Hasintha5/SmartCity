package com.urbantech.smartcity360.chain;

public class HandlingStep {

    private final String handler;
    private final String action;
    private final String note;

    public HandlingStep(String handler, String action, String note) {
        this.handler = handler;
        this.action = action;
        this.note = note;
    }

    public String getHandler() { return handler; }
    public String getAction() { return action; }
    public String getNote() { return note; }
}