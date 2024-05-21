package com.example.semester_6_oop_lab_1_back.dao;

import com.example.semester_6_oop_lab_1_back.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends AbstractDAO {
    private ConversationDAO conversationDAO = new ConversationDAO();
    private ServiceDAO serviceDAO = new ServiceDAO();

    public List<Payment> getPayments(boolean isPaid) {
        final List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE is_paid = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBoolean(1, isPaid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    if (isPaid == resultSet.getBoolean("is_paid")) {
                        final Payment payment = new Payment();
                        payment.setId(resultSet.getInt("id"));
                        payment.setUserId(resultSet.getInt("user_id"));
                        payment.setIsPaid(resultSet.getBoolean("is_paid"));
                        payments.add(payment);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public Payment findById(int id) {
        Payment payment = null;
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    payment = new Payment();
                    payment.setId(resultSet.getInt("id"));
                    payment.setUserId(resultSet.getInt("user_id"));
                    payment.setIsPaid(resultSet.getBoolean("is_paid"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payment;
    }
    public void update(Integer paymentId, boolean isPaid) {
        String sql = "UPDATE payments SET is_paid = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, paymentId);
            preparedStatement.setBoolean(2, isPaid);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pay(Integer paymentId, Double sumToPay) {
        final Payment payment = findById(paymentId);
        if (payment == null) {
            return;
        }
        final List<Conversation> conversations = conversationDAO.findByPaymentId(payment.getId());
        final List<Services> services = serviceDAO.findByPaymentId(payment.getId());

        double totalCost = 0.0;
        for (Conversation conversation : conversations) {
            totalCost += conversation.getMinutes() * conversation.getPriceByMinute();
        }
        for (Services service : services) {
            totalCost += service.getPrice();
        }

        boolean isPaid = sumToPay >= totalCost;
        update(paymentId, isPaid);
    }
}