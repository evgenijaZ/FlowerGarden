package com.flowergarden.dao;

import com.flowergarden.properties.FreshnessInteger;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 18.03.2018.
 */
public abstract class DAO<E, K> implements InterfaceDAO <E, K> {

    String tableName;
    String schemaName;
    private Session session;
    private String SELECT_ALL = "SELECT * FROM %s.%s";
    private String SELECT_BY_ID = "SELECT * FROM %s.%s WHERE %s = ?;";
    private String DELETE_BY_ID = "DELETE FROM %s.%s WHERE %s = ?;";
    private String TRUNCATE_TABLE = "DELETE FROM %s.%s;";


    public DAO(String dbName, String schemaName, String tableName) {
        this.tableName = tableName;
        this.schemaName = schemaName;
        this.session = new Session(dbName);
    }

    protected abstract Class <E> getEntityClass();

    protected abstract Class <K> getKeyClass();

    public abstract String[][] getNameMapping();

    String getUpdateQuery() {
        //"UPDATE %s.%s SET %s=?, ... %s=? WHERE %s=?"
        StringBuilder query = new StringBuilder("UPDATE %s.%s SET ");
        int colCount = getFieldCount() - 1;
        for (int i = 0; i < colCount; i++) {
            query.append("%s=? ,");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append("WHERE %s=?;");
        return String.format(query.toString(), makeFormatArgs(colCount + 1, getNameMapping()));
    }

    String getInsertByIdQuery() {
        int colCount = getFieldCount();
        return String.format(makeInsertQuery(colCount), makeFormatArgs(colCount, getNameMapping()));

    }

    String getInsertQuery() {
        int colCount = getFieldCount() - 1;
        return String.format(makeInsertQuery(colCount), makeFormatArgs(colCount, getNameMapping()));
    }

    String makeInsertQuery(int count) {
        //"INSERT INTO %s.%s (%s, ... %s) VALUES (?, ... ?);";
        StringBuilder query = new StringBuilder("INSERT INTO %s.%s (");
        for (int i = 0; i < count; i++) {
            query.append("%s, ");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(") VALUES (");
        for (int i = 0; i < count; i++) {
            query.append("?, ");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(");");
        return query.toString();
    }

    private String getTruncateTable() {
        return String.format(TRUNCATE_TABLE, schemaName, tableName);
    }

    String getSelectAllQuery() {
        return String.format(SELECT_ALL, schemaName, tableName);
    }

    private String getSelectByIdQuery() {
        String keyFieldName = getNameMapping()[getFieldCount() - 1][1];
        return String.format(SELECT_BY_ID, schemaName, tableName, keyFieldName);
    }

    private String getDeleteByIdQuery() {
        String keyFieldName = getNameMapping()[getNameMapping().length - 1][1];
        return String.format(DELETE_BY_ID, schemaName, tableName, keyFieldName);
    }


    @Override
    @SuppressWarnings("unchecked")
    public List <E> getAll() {
        List <E> entities = new ArrayList <>();
        PreparedStatement statement = session.getPrepareStatement(getSelectAllQuery());
        try {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Object parsingSetResult = this.getEntityFromResultSet(resultSet);
                if (parsingSetResult != null && (getEntityClass()).isInstance(parsingSetResult))
                    entities.add((E) parsingSetResult);
            }
            session.closeStatement(statement);
        } catch (SQLException | IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public boolean update(E entity) {
        boolean result = false;
        PreparedStatement statement = session.getPrepareStatement(getUpdateQuery());
        try {
            statement = prepareStatement(statement, entity);
            result = statement != null && statement.execute();
            session.closeStatement(statement);
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public E getByKey(K key) {
        E entity = null;
        PreparedStatement statement = session.getPrepareStatement(getSelectByIdQuery());
        int idIndex = getFieldCount() - 1;
        try {
            statement = prepareStatementWithOneValue(statement, key, idIndex);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.getMetaData().getColumnCount() != 1)
                if (resultSet.next()) {
                    Object parsingSetResult = this.getEntityFromResultSet(resultSet);
                    if (parsingSetResult != null && (getEntityClass()).isInstance(parsingSetResult))
                        entity = ((E) parsingSetResult);
                }
            session.closeStatement(statement);
        } catch (SQLException | IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            e.printStackTrace();
        }
        return entity;
    }

    public boolean deleteByKey(K key) {
        boolean result = false;
        PreparedStatement statement = session.getPrepareStatement(getDeleteByIdQuery());
        int idIndex = getFieldCount() - 1;
        try {
            statement = prepareStatementWithOneValue(statement, key, idIndex);
            result = statement.execute();
            session.closeStatement(statement);
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    public boolean create(E entity) {
        boolean result = false;
        boolean generateKey;
        try {
            K key = null;
            int keyIndex = getFieldCount() - 1;
            Field field = getField(keyIndex);
            field.setAccessible(true);
            Object value = field.get(entity);

            if (value != null && getKeyClass().isInstance(value))
                key = (K) value;

            generateKey = (key == null) || key.equals(-1);
            PreparedStatement statement;
            if (generateKey)
                statement = session.getPrepareStatement(getInsertQuery());
            else statement = session.getPrepareStatement(getInsertByIdQuery());

            statement = prepareStatement(statement, entity);
            if (statement == null)
                return false;
            result = statement.execute();
            if (generateKey) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        String keyValue = generatedKeys.getString(1);
                        setFieldValue(field, entity, keyValue);
                    } else {
                        throw new SQLException("Creating " + tableName + " failed, no ID obtained.");
                    }
                }
            }
            session.closeStatement(statement);
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
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

    private PreparedStatement prepareStatement(PreparedStatement statement, E item) throws SQLException, NoSuchFieldException, IllegalAccessException {
        int parametersCount = statement.getParameterMetaData().getParameterCount();
        for (int i = 0; i < parametersCount; i++) {
            Field field = getField(i);
            field.setAccessible(true);
            Object value = field.get(item);
            statement = prepareStatementWithOneValue(statement, value, i);
        }
        return statement;
    }

    private Field getField(int index) throws NoSuchFieldException {
        Class <?> entityClass = this.getEntityClass();
        String[][] mapping = getNameMapping();

        String fieldName = mapping[index][0];
        return entityClass.getDeclaredField(fieldName);

    }

    private PreparedStatement prepareStatementWithOneValue(PreparedStatement statement, Object value, int fieldIndex) throws SQLException, NoSuchFieldException, IllegalAccessException {
        String fieldType = getFieldTypeName(fieldIndex);
        if (statement.getParameterMetaData().getParameterCount() <= fieldIndex)
            fieldIndex = 0;
        switch (fieldType) {
            case "String": {
                statement.setString(fieldIndex + 1, value.toString());
                break;
            }
            case "int":
            case "Integer": {
                statement.setInt(fieldIndex + 1, Integer.parseInt(value.toString()));
                break;
            }
            case "Float":
            case "float":
            case "Double":
            case "double": {
                statement.setDouble(fieldIndex + 1, Double.parseDouble(value.toString()));
                break;
            }
            case "boolean":
            case "Boolean": {
                statement.setBoolean(fieldIndex + 1, Boolean.getBoolean(value.toString()));
                break;
            }
            case "Freshness":
            case "FreshnessInteger":
                statement.setInt(fieldIndex + 1, ((FreshnessInteger) value).getFreshness());
                break;
        }
        return statement;
    }

    int getFieldCount() {
        return getNameMapping().length;
    }

    private String getFieldTypeName(int index) throws NoSuchFieldException {
        Field field = getField(index);
        field.setAccessible(true);
        return field.getType().getSimpleName();
    }

    Object[] makeFormatArgs(int count, String[][] nameMapping) {
        List <String> args = new ArrayList <>();
        args.add(schemaName);
        args.add(tableName);
        for (int i = 0; i < count; i++) {
            args.add(nameMapping[i][1]);
        }
        return args.toArray();
    }

    void truncateTable() {
        Statement statement;
        try {
            statement = session.getStatement();
            statement.execute(String.format(getTruncateTable(), schemaName, tableName));
            session.closeStatement(statement);
            statement.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
