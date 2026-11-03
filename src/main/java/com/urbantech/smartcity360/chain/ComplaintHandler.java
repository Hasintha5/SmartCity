package com.urbantech.smartcity360.chain;

public abstract class ComplaintHandler {

    private ComplaintHandler next;

    public ComplaintHandler setNext(ComplaintHandler next) {
        this.next = next;
        return next;
    }

    public ComplaintHandler getNext() {
        return next;
    }

    public abstract String getRole();

    protected abstract boolean canHandle(Complaint complaint);

    protected abstract String resolutionNote();

    public final void handle(Complaint complaint) {
        if (canHandle(complaint)) {
            complaint.addStep(getRole(), "Resolved", resolutionNote());
            complaint.markResolved(getRole());
        } else if (next != null) {
            complaint.addStep(getRole(), "Escalated", "Too serious for this level, passed up.");
            next.handle(complaint);
        } else {
            complaint.addStep(getRole(), "Not resolved",
                    "Too serious for this level and nobody higher is on duty.");
            complaint.markUnresolved();
        }
    }
}