package com.urbantech.smartcity360.web;

import com.urbantech.smartcity360.composite.CityComponent;
import com.urbantech.smartcity360.composite.CityData;
import com.urbantech.smartcity360.composite.CityZone;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "ZoneServlet", urlPatterns = {"/zones"})
public class ZoneServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        show(req, resp, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String target = req.getParameter("target");

        String message;
        synchronized (CityData.class) {
            message = runCommand(action, target);
        }
        show(req, resp, message);
    }

    private String runCommand(String action, String targetName) {
        if ("reset".equals(action)) {
            CityData.reset();
            return "The city was reset. All power is on.";
        }
        if (action == null || targetName == null) {
            return "No command was given.";
        }

        CityComponent target = CityData.getCity().find(targetName);
        if (target == null) {
            return "That zone was not found.";
        }

        String who = target.getLevel() + " " + target.getName();
        switch (action) {
            case "shutdown":
                return "Shutdown sent to " + who + ": " + target.shutdownPower()
                        + " building(s) switched off.";
            case "restore":
                return "Restore sent to " + who + ": " + target.restorePower()
                        + " building(s) switched on.";
            case "report":
                return "Usage report for " + who + ": "
                        + String.format(Locale.ROOT, "%.1f", target.reportUsage())
                        + " kWh from " + target.countBuildings() + " building(s).";
            default:
                return "Unknown command.";
        }
    }

    private void show(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        List<ZoneRow> rows = new ArrayList<>();
        String totalUsage;
        int buildings;

        synchronized (CityData.class) {
            CityZone city = CityData.getCity();
            flatten(city, 0, rows);
            totalUsage = String.format(Locale.ROOT, "%.1f", city.reportUsage());
            buildings = city.countBuildings();
        }

        int buildingsOn = 0;
        for (ZoneRow row : rows) {
            if (row.isLeaf() && row.isPowerOn()) {
                buildingsOn++;
            }
        }

        req.setAttribute("rows", rows);
        req.setAttribute("message", message);
        req.setAttribute("totalUsage", totalUsage);
        req.setAttribute("buildings", buildings);
        req.setAttribute("buildingsOn", buildingsOn);
        req.getRequestDispatcher("/WEB-INF/views/zones.jsp").forward(req, resp);
    }

    private void flatten(CityComponent component, int depth, List<ZoneRow> rows) {
        rows.add(new ZoneRow(component.getName(), component.getLevel(), depth,
                component.reportUsage(), component.isPowerOn(),
                component.countBuildings(), component.getChildren().isEmpty()));
        for (CityComponent child : component.getChildren()) {
            flatten(child, depth + 1, rows);
        }
    }
}