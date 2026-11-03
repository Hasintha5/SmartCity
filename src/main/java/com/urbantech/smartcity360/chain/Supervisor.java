package com.urbantech.smartcity360.chain;

public class Supervisor extends ComplaintHandler {

    @Override
    public String getRole() {
        return "Supervisor";
    }

    @Override
    protected boolean canHandle(Complaint complaint) {
        return complaint.getSeverity().getRank() <= Severity.MEDIUM.getRank();
    }

    @Override
    protected String resolutionNote() {
        return "Reviewed the case and sent a repair team.";
    }
}