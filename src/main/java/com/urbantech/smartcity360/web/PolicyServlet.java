package com.urbantech.smartcity360.web;

import com.urbantech.smartcity360.interpreter.CityContext;
import com.urbantech.smartcity360.interpreter.CityVariable;
import com.urbantech.smartcity360.interpreter.Evaluation;
import com.urbantech.smartcity360.interpreter.Policy;
import com.urbantech.smartcity360.interpreter.PolicyChecker;
import com.urbantech.smartcity360.interpreter.PolicyLibrary;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "PolicyServlet", urlPatterns = {"/policies"})
public class PolicyServlet extends HttpServlet {

    private static final int MAX_RULE_LENGTH = 300;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Policy first = PolicyLibrary.all().get(0);
        show(req, resp, first.getRule(), first.getSample(), null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Policy preset = PolicyLibrary.find(req.getParameter("preset"));
        String rule;
        Map<CityVariable, Double> readings = new EnumMap<>(CityVariable.class);

        if (preset != null) {
            rule = preset.getRule();
            readings.putAll(preset.getSample());
        } else {
            rule = clean(req.getParameter("rule"));
            for (CityVariable variable : CityVariable.values()) {
                readings.put(variable, readNumber(req.getParameter(variable.getKey()), variable));
            }
        }

        CityContext context = new CityContext().setAll(readings);
        Evaluation evaluation = PolicyChecker.check(rule, context);

        show(req, resp, rule, readings, evaluation);
    }

    private void show(HttpServletRequest req, HttpServletResponse resp, String rule,
                      Map<CityVariable, Double> readings, Evaluation evaluation)
            throws ServletException, IOException {
        Map<String, String> values = new LinkedHashMap<>();
        for (CityVariable variable : CityVariable.values()) {
            Double reading = readings.get(variable);
            double shown = reading == null ? variable.getDefaultValue() : reading;
            values.put(variable.getKey(), CityContext.number(shown));
        }

        req.setAttribute("rule", rule);
        req.setAttribute("values", values);
        req.setAttribute("evaluation", evaluation);
        req.setAttribute("policies", PolicyLibrary.all());
        req.setAttribute("variables", CityVariable.values());
        req.getRequestDispatcher("/WEB-INF/views/policies.jsp").forward(req, resp);
    }

    private static String clean(String text) {
        if (text == null) {
            return "";
        }
        String trimmed = text.trim();
        return trimmed.length() > MAX_RULE_LENGTH ? trimmed.substring(0, MAX_RULE_LENGTH) : trimmed;
    }

    private static double readNumber(String text, CityVariable variable) {
        double value = variable.getDefaultValue();
        if (text != null) {
            try {
                double parsed = Double.parseDouble(text.trim());
                if (!Double.isNaN(parsed) && !Double.isInfinite(parsed)) {
                    value = parsed;
                }
            } catch (NumberFormatException e) {
            }
        }
        return Math.max(variable.getMin(), Math.min(variable.getMax(), value));
    }
}