package com.example.semester_6_oop_lab_1_back.dao;

import com.example.semester_6_oop_lab_1_back.model.Services;
import com.example.semester_6_oop_lab_1_back.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.semester_6_oop_lab_1_back.dao.AbstractDAO.getConnection;
import static java.util.stream.Collectors.joining;

public class ServiceDAO {

    private final UserServiceDAO userServiceDAO = new UserServiceDAO();

    public List<Services> findAllServices() {
        final List<Services> services = new ArrayList<>();
        String sql = "SELECT * FROM services";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            handleService(services, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    public List<Services> findByPaymentId(int paymentId) {
        final List<Services> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE payment_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, paymentId);
            handleService(services, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    private void handleService(List<Services> services, PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                final Services service = new Services();
                service.setId(resultSet.getInt("id"));
                service.setPaymentId(resultSet.getInt("payment_id"));
                service.setName(resultSet.getString("name"));
                service.setPrice(resultSet.getDouble("price"));
                services.add(service);
            }
        }
    }

    public void subscribeService(int userId, List<Integer> serviceIds) {
        userServiceDAO.save(userId, serviceIds);
    }
}
