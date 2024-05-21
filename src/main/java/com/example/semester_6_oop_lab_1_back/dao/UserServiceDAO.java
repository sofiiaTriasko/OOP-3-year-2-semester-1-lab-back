package com.example.semester_6_oop_lab_1_back.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserServiceDAO extends AbstractDAO {
    public void save(int userId, List<Integer> serviceIds) {
        serviceIds.forEach(serviceId -> {
            String sql = "INSERT INTO users_services (user_id, services_id) VALUES (?, ?)";
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, userId);
                preparedStatement.setDouble(2, serviceId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
