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

    String tableName;
    String schemaName;
    private Session session;
    private String SELECT_ALL = "SELECT * FROM %s.%s";

    public DAO(String dbName, String schemaName, String tableName) {
        this.tableName = tableName;
        this.schemaName = schemaName;
        this.session = new Session(dbName);
    }

    protected abstract Class <E> getEntityClass();

    protected abstract Class <K> getKeyClass();

    public abstract String[][] getNameMapping();

    abstract String getUpdateQuery();

    String getSelectAllQuery() {
        return String.format(SELECT_ALL, schemaName, tableName);
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
            session.closePrepareStatement(statement);
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
            session.closePrepareStatement(statement);
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
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
        if (statement.getParameterMetaData().getParameterCount() < fieldIndex)
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
                statement.setInt(fieldIndex +1, ((FreshnessInteger)value).getFreshness());
                break;
        }
        return statement;
    }

    int getFieldCount(){
        return getNameMapping().length;
    }

    private String getFieldTypeName(int index) throws NoSuchFieldException {
        Field field = getField(index);
        field.setAccessible(true);
        return field.getType().getSimpleName();
    }

   Object[] makeFormatArgs(int count, String[][] nameMapping){
        List<String> args = new ArrayList <>();
        args.add(schemaName);
        args.add(tableName);
        for(int i=0; i<count; i++){
            args.add(nameMapping[i][1]);
        }
        return args.toArray();
    }
}
