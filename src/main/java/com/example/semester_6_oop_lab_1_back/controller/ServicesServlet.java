package com.example.semester_6_oop_lab_1_back.controller;

import com.example.semester_6_oop_lab_1_back.dao.ServiceDAO;
import com.example.semester_6_oop_lab_1_back.model.Services;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/services")
public class ServicesServlet extends HttpServlet {

    
    private final ServiceDAO serviceDAO = new ServiceDAO();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final int userId = Integer.parseInt(req.getParameter("userId"));
        final List<Integer> serviceIds = Arrays.stream(req.getParameterValues("serviceIds")).map(Integer::parseInt).collect(Collectors.toList());

        serviceDAO.subscribeService(userId, serviceIds);

        PrintWriter out = resp.getWriter();
        out.print("success");
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String paymentId = req.getParameter("paymentId");
        List<Services> services;
        if (paymentId == null) {
            services = serviceDAO.findAllServices();
        } else {
            services = serviceDAO.findByPaymentId(Integer.parseInt(paymentId));
        }
        resp.setContentType("application/json");
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(services);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}