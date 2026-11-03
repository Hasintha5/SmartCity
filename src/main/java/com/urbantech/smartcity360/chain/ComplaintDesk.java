package com.urbantech.smartcity360.chain;

import java.util.ArrayList;
import java.util.List;

public final class ComplaintDesk {

    private static final List<Complaint> COMPLAINTS = new ArrayList<>();
    private static int nextId = 1;

    private ComplaintDesk() {
    }

    public static ComplaintHandler buildChain(boolean staffOn, boolean supervisorOn, boolean managerOn) {
        List<ComplaintHandler> onDuty = new ArrayList<>();
        if (staffOn) {
            onDuty.add(new LocalStaff());
        }
        if (supervisorOn) {
            onDuty.add(new Supervisor());
        }
        if (managerOn) {
            onDuty.add(new CityManager());
        }

        for (int i = 0; i < onDuty.size() - 1; i++) {
            onDuty.get(i).setNext(onDuty.get(i + 1));
        }
        return onDuty.isEmpty() ? null : onDuty.get(0);
    }

    public static List<String> roles(ComplaintHandler head) {
        List<String> roles = new ArrayList<>();
        for (ComplaintHandler h = head; h != null; h = h.getNext()) {
            roles.add(h.getRole());
        }
        return roles;
    }

    public static synchronized Complaint submit(ComplaintHandler head, String citizen,
                                                String category, String description,
                                                Severity severity) {
        Complaint complaint = new Complaint(nextId++, citizen, category, description, severity);

        if (head == null) {
            complaint.addStep("Nobody", "Not resolved", "No officer is on duty.");
            complaint.markUnresolved();
        } else {
            head.handle(complaint);
        }

        COMPLAINTS.add(0, complaint);
        return complaint;
    }

    public static synchronized List<Complaint> getAll() {
        return new ArrayList<>(COMPLAINTS);
    }

    public static synchronized void clear() {
        COMPLAINTS.clear();
        nextId = 1;
    }
}