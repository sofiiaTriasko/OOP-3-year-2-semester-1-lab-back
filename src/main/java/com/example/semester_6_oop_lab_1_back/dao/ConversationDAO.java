package com.example.semester_6_oop_lab_1_back.dao;

import com.example.semester_6_oop_lab_1_back.model.Conversation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
