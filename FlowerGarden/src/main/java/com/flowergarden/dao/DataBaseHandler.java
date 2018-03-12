package com.flowergarden.dao;

import java.io.File;
import java.io.IOException;
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
        File file = new File(pathToDB);
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + file.getCanonicalFile().toURI());
        } catch (SQLException | IOException e) {
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
            if (connection != null) {
                Statement statement = connection.createStatement();
                statement.execute(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            Connection connection = openConnection();
            if (connection != null) {
                Statement statement = connection.createStatement();
                return statement.executeQuery(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
