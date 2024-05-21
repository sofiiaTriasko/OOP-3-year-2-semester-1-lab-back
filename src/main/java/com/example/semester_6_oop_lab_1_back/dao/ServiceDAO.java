package com.example.semester_6_oop_lab_1_back.dao;

import com.example.semester_6_oop_lab_1_back.dto.PaymentDataDTO;
import com.example.semester_6_oop_lab_1_back.dto.ServicesDataDTO;
import com.example.semester_6_oop_lab_1_back.model.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.semester_6_oop_lab_1_back.dao.AbstractDAO.getConnection;

public class ServiceDAO {

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

    public List<ServicesDataDTO> findByIdInData(List<Integer> servicesIds) {
        final List<Services> services = new ArrayList<>();
        final List<ServicesDataDTO> servicesData = new ArrayList<>();

        // Create a comma-separated list of placeholders
        final String placeholders = servicesIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql = "SELECT * FROM services WHERE id in (" + placeholders + ")";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < servicesIds.size(); i++) {
                preparedStatement.setInt(i + 1, servicesIds.get(i));
            }
            handleService(services, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Services service : services) {
            ServicesDataDTO servicesDataDTO = new ServicesDataDTO();
            servicesDataDTO.setId(service.getId());
            servicesDataDTO.setName(service.getName());
            servicesDataDTO.setPrice(service.getPrice());
            servicesDataDTO.setPaymentDataDTO(findByIdData(service.getPaymentId()));
            servicesData.add(servicesDataDTO);
        }

        return servicesData;
    }

    private PaymentDataDTO findByIdData(int id) {
        PaymentDataDTO payment = null;
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    payment = new PaymentDataDTO();
                    payment.setId(resultSet.getInt("id"));
                    payment.setIsPaid(resultSet.getBoolean("is_paid"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payment;
    }
}
