package com.urbantech.smartcity360.chain;

public class CityManager extends ComplaintHandler {

    @Override
    public String getRole() {
        return "City manager";
    }

    @Override
    protected boolean canHandle(Complaint complaint) {
        return complaint.getSeverity().getRank() <= Severity.HIGH.getRank();
    }

    @Override
    protected String resolutionNote() {
        return "Approved an emergency response and informed the city council.";
    }
}