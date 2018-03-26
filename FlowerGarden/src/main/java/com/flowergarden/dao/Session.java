package com.flowergarden.dao;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * @author Yevheniia Zubrych on 15.03.2018.
 */
class Session {

    DataSource dataSource;
    private Connection connection;


    Session(DataSource dataSource) {
        this.dataSource=dataSource;
        connection = openConnection();
        if(connection == null)
            System.out.println("null");
    }

     private Connection openConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
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

    void setAutoCommit(boolean autoCommit){
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void commit(){
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void rollback(){
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






}