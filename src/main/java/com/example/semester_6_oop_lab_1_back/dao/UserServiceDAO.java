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

    public List<ServicesDataDTO> findByUserIds(List<Integer> userIds) {
        final Set<Integer> servicesIds = new HashSet<>();

        String sql = "SELECT * FROM users_services WHERE user_id in (?)";
        String ids = userIds.stream().map(String::valueOf)
                .collect(Collectors.joining(","));

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ids);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
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
