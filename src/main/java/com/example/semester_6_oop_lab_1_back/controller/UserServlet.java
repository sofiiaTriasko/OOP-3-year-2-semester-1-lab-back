package com.example.semester_6_oop_lab_1_back.controller;

import com.example.semester_6_oop_lab_1_back.dao.UserDAO;
import com.example.semester_6_oop_lab_1_back.dto.UserDataDTO;
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

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String number = req.getParameter("number");
        final List<UserDataDTO> result = userDAO.findByNumberData(number);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(result);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
