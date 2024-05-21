package com.example.semester_6_oop_lab_1_back.controller;

import com.example.semester_6_oop_lab_1_back.dao.PaymentDAO;
import com.example.semester_6_oop_lab_1_back.model.Payment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;


@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private final PaymentDAO paymentDAO = new PaymentDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPaid = Boolean.parseBoolean(request.getParameter("isPaid"));
        final List<Payment> payments = paymentDAO.getPayments(isPaid);

        response.setContentType("application/json");
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(payments);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer paymentId = Objects.isNull(request.getParameter("paymentId")) ? null : Integer.parseInt(request.getParameter("paymentId"));
        Double sumToPay = Double.valueOf(request.getParameter("sum"));

        paymentDAO.pay(paymentId, sumToPay);
        PrintWriter out = response.getWriter();
        out.print("Payment paid");
        out.flush();
    }
}