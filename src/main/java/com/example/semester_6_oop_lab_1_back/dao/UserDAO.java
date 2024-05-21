package com.example.semester_6_oop_lab_1_back.dao;


import com.example.semester_6_oop_lab_1_back.dto.UserDataDTO;
import com.example.semester_6_oop_lab_1_back.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDAO extends AbstractDAO {

    private final ConversationDAO conversationDAO = new ConversationDAO();
    private final UserServiceDAO userServiceDAO = new UserServiceDAO();

    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setMoney(resultSet.getDouble("money"));
                user.setPassword(resultSet.getString("password"));
                user.setNumber(resultSet.getString("number"));
                user.setIsBlocked(resultSet.getBoolean("is_blocked"));
                userList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public User findById(int id) {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setMoney(resultSet.getDouble("money"));
                    user.setPassword(resultSet.getString("password"));
                    user.setNumber(resultSet.getString("number"));
                    user.setIsBlocked(resultSet.getBoolean("is_blocked"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User findByName(String name) {
        User user = null;
        String sql = "SELECT * FROM users WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setMoney(resultSet.getDouble("money"));
                    user.setPassword(resultSet.getString("password"));
                    user.setNumber(resultSet.getString("number"));
                    user.setIsBlocked(resultSet.getBoolean("is_blocked"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<User> findByNumber(String number) {
        if (number == null) {
            return findAll();
        }

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE number like ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + number + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setMoney(resultSet.getDouble("money"));
                    user.setPassword(resultSet.getString("password"));
                    user.setNumber(resultSet.getString("number"));
                    user.setIsBlocked(resultSet.getBoolean("is_blocked"));
                    users.add(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (name, money, password, number, is_blocked) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setDouble(2, user.getMoney());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getNumber());
            preparedStatement.setBoolean(5, user.getIsBlocked());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET name = ?, money = ?, password = ?, number = ?, is_blocked = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setDouble(2, user.getMoney());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getNumber());
            preparedStatement.setBoolean(5, user.getIsBlocked());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void blockUser(int userId) {
        String sql = "UPDATE users SET is_blocked = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UserDataDTO> findByNumberData(String number) {
        final List<User> users = findByNumber(number);
        final List<Integer> ids = users.stream().map(User::getId).collect(Collectors.toList());
        final List<UserDataDTO> result = new ArrayList<>();

        for (User user : users) {
            UserDataDTO userDataDTO = new UserDataDTO();
            userDataDTO.setId(user.getId());
            userDataDTO.setName(user.getName());
            userDataDTO.setMoney(user.getMoney());
            userDataDTO.setPassword(user.getPassword());
            userDataDTO.setNumber(user.getNumber());
            userDataDTO.setIsBlocked(user.getIsBlocked());
            userDataDTO.setConversations(conversationDAO.findByUserIds(ids));
            userDataDTO.setServices(userServiceDAO.findByUserIds(ids));
            result.add(userDataDTO);
        }

        return result;
    }
}