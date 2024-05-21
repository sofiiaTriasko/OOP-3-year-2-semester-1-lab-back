package com.example.semester_6_oop_lab_1_back.dao;


import com.example.semester_6_oop_lab_1_back.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDAO {

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
}