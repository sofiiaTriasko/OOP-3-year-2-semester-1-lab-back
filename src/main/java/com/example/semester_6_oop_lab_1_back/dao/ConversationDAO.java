package com.example.semester_6_oop_lab_1_back.dao;

import com.example.semester_6_oop_lab_1_back.dto.ConversationDataDTO;
import com.example.semester_6_oop_lab_1_back.dto.PaymentDataDTO;
import com.example.semester_6_oop_lab_1_back.model.Conversation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConversationDAO extends AbstractDAO {


    public List<Conversation> findByPaymentId(int paymentId) {
        final List<Conversation> conversations = new ArrayList<>();
        String sql = "SELECT * FROM conversations WHERE payment_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, paymentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    final Conversation conversation = new Conversation();
                    conversation.setId(resultSet.getInt("id"));
                    conversation.setUserId(resultSet.getInt("user_id"));
                    conversation.setPaymentId(resultSet.getInt("payment_id"));
                    conversation.setPriceByMinute(resultSet.getDouble("price_by_minute"));
                    conversation.setMinutes(resultSet.getLong("minutes"));
                    conversations.add(conversation);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conversations;
    }

    public List<ConversationDataDTO> findByUserIds(List<Integer> userIds) {
        final List<Conversation> conversations = new ArrayList<>();
        final List<ConversationDataDTO> conversationDataDTOS = new ArrayList<>();

        // Create a comma-separated list of placeholders
        final String placeholders = userIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        final String sql = "SELECT * FROM conversations WHERE user_id in (" + placeholders + ")";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < userIds.size(); i++) {
                preparedStatement.setInt(i + 1, userIds.get(i));
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    final Conversation conversation = new Conversation();
                    conversation.setId(resultSet.getInt("id"));
                    conversation.setUserId(resultSet.getInt("user_id"));
                    conversation.setPaymentId(resultSet.getInt("payment_id"));
                    conversation.setPriceByMinute(resultSet.getDouble("price_by_minutes"));
                    conversation.setMinutes(resultSet.getLong("minutes"));
                    conversations.add(conversation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Conversation conversation : conversations) {
            ConversationDataDTO conversationDataDTO = new ConversationDataDTO();
            conversationDataDTO.setId(conversation.getId());
            conversationDataDTO.setPriceByMinute(conversationDataDTO.getPriceByMinute());
            conversationDataDTO.setMinutes(conversationDataDTO.getMinutes());
            conversationDataDTO.setPayment(findByIdData(conversation.getPaymentId()));
            conversationDataDTOS.add(conversationDataDTO);
        }

        return conversationDataDTOS;
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
