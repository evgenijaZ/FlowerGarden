package com.flowergarden.dao;


import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * @author Yevheniia Zubrych on 15.03.2018.
 */
class Session {

    private final String URL = "jdbc:sqlite:";
    private String dbName;
    private Connection connection;


    Session(String dbName) {
        this.dbName = dbName;
        connection = openConnection();
    }

    private Connection openConnection() {
        File file = new File(dbName+".db");
        try {
            return DriverManager.getConnection(URL + file.getCanonicalFile().toURI());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Statement getStatement(){
        try{
            if(connection.isClosed())
                connection = openConnection();
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    PreparedStatement getPrepareStatement(String sql) {
        PreparedStatement preparedStatement = null;
        try {
            if(connection.isClosed()) connection = openConnection();
            if (connection != null) {
                preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }


    void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}