package com.urbantech.smartcity360.web;

import com.urbantech.smartcity360.decorator.AuthenticationDecorator;
import com.urbantech.smartcity360.decorator.CompressionDecorator;
import com.urbantech.smartcity360.decorator.DataStream;
import com.urbantech.smartcity360.decorator.EncryptionDecorator;
import com.urbantech.smartcity360.decorator.EnergyMeterStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "StreamServlet", urlPatterns = {"/streams"})
public class StreamServlet extends HttpServlet {

    private static final String SAMPLE_READING = String.join("\n",
            "meterId=EM-1042;zone=Colombo-03;time=2026-10-30T12:00;kWh=3.82;voltage=229.7",
            "meterId=EM-1042;zone=Colombo-03;time=2026-10-30T12:05;kWh=3.85;voltage=230.1",
            "meterId=EM-1042;zone=Colombo-03;time=2026-10-30T12:10;kWh=3.91;voltage=229.4",
            "meterId=EM-1042;zone=Colombo-03;time=2026-10-30T12:15;kWh=3.97;voltage=229.9");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("reading", SAMPLE_READING);
        req.setAttribute("compress", true);
        req.setAttribute("encrypt", true);
        req.setAttribute("auth", true);
        showPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String reading = req.getParameter("reading");
        if (reading == null) {
            reading = "";
        }

        boolean compress = req.getParameter("compress") != null;
        boolean encrypt = req.getParameter("encrypt") != null;
        boolean auth = req.getParameter("auth") != null;

        DataStream stream = new EnergyMeterStream();
        if (compress) {
            stream = new CompressionDecorator(stream);
        }
        if (encrypt) {
            stream = new EncryptionDecorator(stream);
        }
        if (auth) {
            stream = new AuthenticationDecorator(stream);
        }

        String result = stream.process(reading);

        req.setAttribute("reading", reading);
        req.setAttribute("compress", compress);
        req.setAttribute("encrypt", encrypt);
        req.setAttribute("auth", auth);
        req.setAttribute("result", result);
        req.setAttribute("description", stream.describe());
        req.setAttribute("rawLength", reading.length());
        req.setAttribute("resultLength", result.length());
        showPage(req, resp);
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/streams.jsp").forward(req, resp);
    }
}