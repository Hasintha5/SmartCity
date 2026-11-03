package com.urbantech.smartcity360.web;

import com.urbantech.smartcity360.flyweight.FleetComparison;
import com.urbantech.smartcity360.flyweight.StreetLight;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "StreetLightServlet", urlPatterns = {"/streetlights"})
public class StreetLightServlet extends HttpServlet {

    private static final List<Integer> COUNTS = List.of(1000, 10000, 25000, 50000);
    private static final int DEFAULT_COUNT = 10000;
    private static final int DEFAULT_HOUR = 22;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        show(req, resp, DEFAULT_COUNT, DEFAULT_HOUR);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        int count = parse(req.getParameter("count"), DEFAULT_COUNT);
        if (!COUNTS.contains(count)) {
            count = DEFAULT_COUNT;
        }
        int hour = Math.max(0, Math.min(23, parse(req.getParameter("hour"), DEFAULT_HOUR)));

        FleetComparison.Result result = FleetComparison.run(count);

        List<String> samples = new ArrayList<>();
        for (StreetLight light : result.getSample()) {
            samples.add(light.status(hour));
        }

        req.setAttribute("result", result);
        req.setAttribute("samples", samples);
        show(req, resp, count, hour);
    }

    private void show(HttpServletRequest req, HttpServletResponse resp, int count, int hour)
            throws ServletException, IOException {
        req.setAttribute("count", count);
        req.setAttribute("hour", hour);
        req.setAttribute("counts", COUNTS);
        req.setAttribute("scaling", FleetComparison.scalingTable());
        req.getRequestDispatcher("/WEB-INF/views/streetlights.jsp").forward(req, resp);
    }

    private static int parse(String text, int fallback) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            return fallback;
        }
    }
}