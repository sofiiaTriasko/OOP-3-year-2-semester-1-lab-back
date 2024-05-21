package com.example.semester_6_oop_lab_1_back.controller;

import com.example.semester_6_oop_lab_1_back.dao.UserDAO;
import com.example.semester_6_oop_lab_1_back.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class UserLoginServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        User user = userDao.findByName(name);
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            result.put("success", true);
            result.put("user", user);
            response.setContentType("application/json");
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(result);
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        } else {
            result.put("success", false);
            result.put("message", "Invalid username or password");
            response.setContentType("application/json");
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(result);
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        }
    }
}