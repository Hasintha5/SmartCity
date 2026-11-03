package com.urbantech.smartcity360.web;

import com.urbantech.smartcity360.chain.Complaint;
import com.urbantech.smartcity360.chain.ComplaintDesk;
import com.urbantech.smartcity360.chain.ComplaintHandler;
import com.urbantech.smartcity360.chain.Severity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ComplaintServlet", urlPatterns = {"/complaints"})
public class ComplaintServlet extends HttpServlet {

    private static final List<String> CATEGORIES = List.of(
            "Garbage collection", "Street light", "Water leak", "Noise", "Road damage", "Other");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setDefaults(req);
        show(req, resp, true, true, true);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        boolean staffOn = req.getParameter("staff") != null;
        boolean supervisorOn = req.getParameter("supervisor") != null;
        boolean managerOn = req.getParameter("manager") != null;

        setDefaults(req);

        if ("clear".equals(req.getParameter("action"))) {
            ComplaintDesk.clear();
            req.setAttribute("message", "All complaints were cleared.");
            show(req, resp, staffOn, supervisorOn, managerOn);
            return;
        }

        String citizen = clean(req.getParameter("citizen"), 60);
        String category = clean(req.getParameter("category"), 40);
        String description = clean(req.getParameter("description"), 500);
        Severity severity = Severity.fromParam(req.getParameter("severity"));
        if (!CATEGORIES.contains(category)) {
            category = "Other";
        }

        req.setAttribute("fCitizen", citizen);
        req.setAttribute("fCategory", category);
        req.setAttribute("fSeverity", severity.getCode());

        if (description.isEmpty()) {
            req.setAttribute("message", "Please describe the problem before sending.");
        } else {
            ComplaintHandler head = ComplaintDesk.buildChain(staffOn, supervisorOn, managerOn);
            String name = citizen.isEmpty() ? "Anonymous resident" : citizen;
            Complaint latest = ComplaintDesk.submit(head, name, category, description, severity);
            req.setAttribute("latest", latest);
        }
        show(req, resp, staffOn, supervisorOn, managerOn);
    }

    private void setDefaults(HttpServletRequest req) {
        req.setAttribute("fCitizen", "");
        req.setAttribute("fCategory", CATEGORIES.get(0));
        req.setAttribute("fDescription", "");
        req.setAttribute("fSeverity", Severity.LOW.getCode());
    }

    private void show(HttpServletRequest req, HttpServletResponse resp,
                      boolean staffOn, boolean supervisorOn, boolean managerOn)
            throws ServletException, IOException {
        ComplaintHandler head = ComplaintDesk.buildChain(staffOn, supervisorOn, managerOn);

        req.setAttribute("staffOn", staffOn);
        req.setAttribute("supervisorOn", supervisorOn);
        req.setAttribute("managerOn", managerOn);
        req.setAttribute("chainRoles", ComplaintDesk.roles(head));
        req.setAttribute("categories", CATEGORIES);
        req.setAttribute("severities", Severity.values());
        req.setAttribute("complaints", ComplaintDesk.getAll());
        req.getRequestDispatcher("/WEB-INF/views/complaints.jsp").forward(req, resp);
    }

    private static String clean(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        String trimmed = text.trim();
        return trimmed.length() > maxLength ? trimmed.substring(0, maxLength) : trimmed;
    }
}