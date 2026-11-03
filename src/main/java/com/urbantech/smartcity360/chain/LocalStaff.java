package com.urbantech.smartcity360.chain;

public class LocalStaff extends ComplaintHandler {

    @Override
    public String getRole() {
        return "Local staff";
    }

    @Override
    protected boolean canHandle(Complaint complaint) {
        return complaint.getSeverity().getRank() <= Severity.LOW.getRank();
    }

    @Override
    protected String resolutionNote() {
        return "Visited the place and fixed the problem on the spot.";
    }
}