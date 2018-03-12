package com.flowergarden.dao;

import java.sql.*;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 07.03.2018.
 */
public abstract class AbstractDAO<E, K> {
    private Connection connection;

    public AbstractDAO(Connection connection){
        this.connection = connection;
    }

    public abstract List <E> getAll() throws SQLException;

    public abstract E update(E entity);

    public abstract E getById(K id);

    public abstract boolean delete(E entity);

    public abstract boolean create(E entity);

    private void execute(String query) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } else throw new IllegalStateException();
    }

    public ResultSet executeQuery(String query) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        }
        return null;
    }
}