package com.example.semester_6_oop_lab_1_back.dao;

import com.example.semester_6_oop_lab_1_back.dto.ServicesDataDTO;
import com.example.semester_6_oop_lab_1_back.model.Services;
import com.example.semester_6_oop_lab_1_back.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserServiceDAO extends AbstractDAO {

    private final ServiceDAO serviceDAO = new ServiceDAO();

    public List<ServicesDataDTO> findByUserIds(List<Integer> userIds) {
        final List<Integer> servicesIds = new ArrayList<>();

        // Create a comma-separated list of placeholders
        final String placeholders = userIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        final String sql = "SELECT * FROM users_services WHERE user_id IN (" + placeholders + ")";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set each user ID in the PreparedStatement
            for (int i = 0; i < userIds.size(); i++) {
                preparedStatement.setInt(i + 1, userIds.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    servicesIds.add(Integer.valueOf(resultSet.getString("services_id")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (servicesIds.isEmpty()) {
            return new ArrayList<>();
        }

        return serviceDAO.findByIdInData(servicesIds);
    }
}
