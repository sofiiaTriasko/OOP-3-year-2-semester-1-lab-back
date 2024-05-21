package com.example.semester_6_oop_lab_1_back.dao;

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

    private final UserServiceDAO userServiceDAO = new UserServiceDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

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

    public List<ServicesDataDTO> findByIdInData(Set<Integer> servicesIds) {
        final List<Services> services = new ArrayList<>();
        final List<ServicesDataDTO> servicesData = new ArrayList<>();
        String ids = servicesIds.stream().map(String::valueOf)
                .collect(Collectors.joining(","));
        String sql = "SELECT * FROM services WHERE id in (?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, ids);
            handleService(services, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        services.forEach(service -> {
            ServicesDataDTO servicesDataDTO = new ServicesDataDTO();
            servicesDataDTO.setId(service.getId());
            servicesDataDTO.setName(service.getName());
            servicesDataDTO.setPrice(service.getPrice());
            servicesDataDTO.setPaymentDataDTO(paymentDAO.findByIdData(service.getPaymentId()));
            servicesData.add(servicesDataDTO);
        });

        return servicesData;
    }
}
