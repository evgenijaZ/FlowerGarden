package com.flowergarden.dao;

import com.flowergarden.properties.FreshnessInteger;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 18.03.2018.
 */
public abstract class DAO<E, K> implements InterfaceDAO <E, K> {

    private Session session;
    String tableName;
    String schemaName;


    private String SELECT_ALL = "SELECT * FROM %s.%s";

    public DAO(String dbName, String schemaName, String tableName) {
        this.tableName = tableName;
        this.schemaName = schemaName;
        this.session = new Session(dbName);
    }

    protected abstract Class <E> getEntityClass();

    protected abstract Class <K> getKeyClass();

    public abstract String[][] getNameMapping();

    private String getSelectAllQuery() {
        return String.format(SELECT_ALL, schemaName, tableName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List <E> getAll() {
        List <E> entities = new ArrayList<>();
        PreparedStatement statement = session.getPrepareStatement(getSelectAllQuery());
        try {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Object parsingSetResult = this.getEntityFromResultSet(resultSet);
                if (parsingSetResult != null && (getEntityClass()).isInstance(parsingSetResult))
                    entities.add((E) parsingSetResult);
            }
            session.closePrepareStatement(statement);
        } catch (SQLException | IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public boolean update(E entity) {
        return false;
    }

    @Override
    public E getByKey(K key) {
        return null;
    }

    @Override
    public boolean deleteByKey(K key) {
        return false;
    }

    @Override
    public boolean create(E entity) {
        return false;
    }


    private Object getEntityFromResultSet(ResultSet resultSet) throws IllegalAccessException, InstantiationException, NoSuchFieldException, SQLException {
        Class <?> entityClass = this.getEntityClass();
        Object instance = entityClass.newInstance();
        for (String[] columnFieldPair : (getNameMapping())) {
                String fieldName = columnFieldPair[0];
                String columnName = columnFieldPair[1];

                Field field = entityClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                String value = resultSet.getString(columnName);

                setFieldValue(field, instance, value);
            }
        return instance;
    }
    private void setFieldValue(Field field, Object instance, String value) throws IllegalAccessException {
        String fieldType = field.getType().getSimpleName();
        switch (fieldType) {
            case "String": {
                field.set(instance, value);
                break;
            }
            case "int":
            case "Integer": {
                field.setInt(instance, Integer.parseInt(value));
                break;
            }
            case "Double":
            case "double":
            case "Float":
            case "float": {
                field.setFloat(instance, Float.parseFloat(value));
                break;
            }
            case "boolean":
            case "Boolean": {
                field.setBoolean(instance, Boolean.getBoolean(value));
                break;
            }
            case "Freshness":
            case "FreshnessInteger":
                field.set(instance, new FreshnessInteger(Integer.parseInt(value)));
                break;
            }
        }
}
