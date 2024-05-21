package com.example.semester_6_oop_lab_1_back.controller;

import com.example.semester_6_oop_lab_1_back.Util.Utils;
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

@WebServlet("/register")
public class UserRegistrationServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        Double money = Double.parseDouble(request.getParameter("money"));
        User user = new User(Utils.randomId(), name, money, password, "", false);
        userDao.save(user);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        response.setContentType("application/json");
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(result);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}


