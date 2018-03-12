package com.flowergarden.dao;

import java.sql.*;

/**
 * @author Yevheniia Zubrych on 12.03.2018.
 */
public class DataBaseHandler {
    private String pathToDB;

    public DataBaseHandler(String pathToDB) {
        this.pathToDB = pathToDB;
    }

    public DataBaseHandler() {
        this.pathToDB = "flowergarden.db";
    }

    public Connection openConnection() {
        String url = "jdbc:sqlite:" + pathToDB;
        try (Connection connection = DriverManager.getConnection(url)) {
            Statement statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection(Connection connection) {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    private void execute(String query) {
        try (Connection connection = openConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(query);
            closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ResultSet executeQuery(String query) throws SQLException {
        try (Connection connection = openConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
