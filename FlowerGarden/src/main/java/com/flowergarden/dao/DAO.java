package com.flowergarden.dao;

import com.flowergarden.properties.FreshnessInteger;

import javax.sql.DataSource;
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

    Session session;
    DataSource dataSource;
    String schemaName;
    private String tableName;
    private String SELECT_ALL = "SELECT * FROM %s.%s";
    private String SELECT_BY_ID = "SELECT * FROM %s.%s WHERE %s = ?;";
    private String DELETE_BY_ID = "DELETE FROM %s.%s WHERE %s = ?;";
    private String TRUNCATE_TABLE = "DELETE FROM %s.%s;";


    DAO(DataSource dataSource, String schemaName, String tableName) {
        this.tableName = tableName;
        this.schemaName = schemaName;
        this.session = new Session(dataSource);
        this.dataSource = dataSource;
    }

    protected abstract Class <E> getEntityClass();

    protected abstract Class <K> getKeyClass();

    /**
     * Returns field name - column name mapping.
     * Id column name should be the last one.
     *
     * @return name mapping
     */
    public abstract String[][] getNameMapping();

    /**
     * Makes query like as "UPDATE %s.%s SET %s=?, ... , %s=? WHERE %s=?"
     * and fills with schema name, table name, column names.
     * Arguments count depends on number of declared fields name mapping.
     *
     * @return `update by id` query
     */
    private String getUpdateQuery() {
        StringBuilder query = new StringBuilder("UPDATE %s.%s SET ");
        int colCount = getFieldCount() - 1;
        for (int i = 0; i < colCount; i++) {
            query.append("%s=? ,");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append("WHERE %s=?;");
        return String.format(query.toString(), makeFormatArgs(colCount + 1));
    }

    /**
     * Fills the `insert by id` query with schema name, table name, column names
     *
     * @return `insert by id` query
     */
    private String getInsertByIdQuery() {
        int colCount = getFieldCount();
        return String.format(makeInsertQuery(colCount), makeFormatArgs(colCount));

    }

    /**
     * Fills the `insert` query with schema name, table name, column names
     *
     * @return `insert` query
     */
    private String getInsertQuery() {
        int colCount = getFieldCount() - 1;
        return String.format(makeInsertQuery(colCount), makeFormatArgs(colCount));
    }


    /**
     * Returns query like as "INSERT INTO %s.%s (%s, ... %s) VALUES (?, ... ?);".
     * Arguments count depends on number of declared fields name mapping.
     *
     * @return insert query
     */
    private String makeInsertQuery(int count) {
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

    /**
     * Makes `delete table_name` query and fills with schema name and table name
     *
     * @return `delete table_name` query
     */
    private String getTruncateTable() {
        return String.format(TRUNCATE_TABLE, schemaName, tableName);
    }

    /**
     * Makes `select all` query and fills with schema name and table name
     *
     * @return `select all` query
     */
    String getSelectAllQuery() {
        return String.format(SELECT_ALL, schemaName, tableName);
    }

    /**
     * Makes `select all` query for object that stores in two tables: parent and child(current) via `join`
     *
     * @return `select all` query
     */
    String getSelectAllQueryWithParent(String[][] parentMapping, String parentTableName, String foreignKey) {
        StringBuilder selectAllQuery = new StringBuilder("SELECT ");
        String[][] currentMapping = this.getNameMapping();
        for (String[] map : parentMapping) {
            String columnName = map[1];
            selectAllQuery
                    .append(parentTableName).append(".").append(columnName)
                    .append(" as ").append(columnName).append(",  ");

        }
        for (String[] map : currentMapping) {
            String columnName = map[1];
            selectAllQuery
                    .append(tableName).append(".").append(columnName)
                    .append(" as ").append(columnName).append(",  ");

        }
        selectAllQuery.deleteCharAt(selectAllQuery.lastIndexOf(","));
        selectAllQuery.append("FROM ").append(parentTableName)
                .append(" JOIN ").append(tableName)
                .append(" ON  ").append(parentTableName).append(".id").append("=").append(tableName).append(".").append(foreignKey);
        return selectAllQuery.toString();
    }

    /**
     * Makes `select by id` query and fills with schema name and table name, name of id column
     *
     * @return `select by id` query
     */
    private String getSelectByIdQuery() {
        String keyFieldName = getNameMapping()[getFieldCount() - 1][1];
        return String.format(SELECT_BY_ID, schemaName, tableName, keyFieldName);
    }

    /**
     * Makes `select by id` query and fills with schema name and table name, name of id column
     *
     * @return `select by id` query
     */
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
        } catch (SQLException e) {
            System.err.println("Cannot execute 'select all' query: " + e.getMessage());
        } catch (IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            System.err.println("Cannot get entity from result set: " + e.getMessage());
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
        } catch (IllegalAccessException e) {
            System.err.println("Cannot execute 'update' query: " + e.getMessage());
        } catch (SQLException | NoSuchFieldException e) {
            System.err.println("Cannot prepare statement for 'update' query: " + e.getMessage());
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
            if (statement == null)
                return false;
            statement = prepareStatement(statement, entity);

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

    /*
     * Inserts both in current and in the parent table
     */
    @SuppressWarnings("unchecked")
    boolean createWithParent(E item, String parentTableName, String[][] parentNameMapping, Class parentClass, Object parentItem, String foreignKey) {
        boolean generateKey;

        K key = null;
        int keyIndex = getFieldCount() - 1;
        Field field = null;
        Object value = null;
        try {
            field = getField(keyIndex);
            field.setAccessible(true);
            value = field.get(item);
        } catch (NoSuchFieldException e) {
            System.err.println("No such field for " + item.getClass().getSimpleName() + " with index " + keyIndex + " :" + e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.println("Illegal access while creating new item " + e.getMessage());
        }

        if (value != null && getKeyClass().isInstance(value))
            key = (K) value;

        generateKey = (key == null) || key.equals(-1);

        int parentColumnCount = parentNameMapping.length;
        int childColumnCount = getFieldCount();

        String parentInsertQuery;
        if (generateKey)
            parentInsertQuery = String.format(makeInsertQuery(parentColumnCount - 1),
                    makeFormatArgs(parentColumnCount - 1, parentTableName, parentNameMapping));
        else
            parentInsertQuery = String.format(makeInsertQuery(parentColumnCount),
                    makeFormatArgs(parentColumnCount, parentTableName, parentNameMapping));

        String childInsertQuery = String.format(makeInsertQuery(childColumnCount - 1),
                makeFormatArgs(childColumnCount, tableName, getNameMapping()));

        String setIdQuery = String.format("UPDATE %s.%s SET %s=? WHERE %s=?;", schemaName, tableName, foreignKey, getNameMapping()[1][1]);


        PreparedStatement parentInsertStatement = session.getPrepareStatement(parentInsertQuery);
        PreparedStatement childInsertStatement = session.getPrepareStatement(childInsertQuery);
        PreparedStatement setIdStatement = session.getPrepareStatement(setIdQuery);


        try {
            session.setAutoCommit(false);

            prepareStatement(parentInsertStatement, parentClass, parentItem, parentNameMapping);
            parentInsertStatement.execute();

            String parentIdValue = "";
            if (generateKey) {
                try (ResultSet generatedKeys = parentInsertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        parentIdValue = generatedKeys.getString(1);
                        setFieldValue(field, item, parentIdValue);
                    } else {
                        throw new SQLException("Creating " + parentTableName + " failed, no ID obtained.");
                    }
                }
            }
            session.closeStatement(parentInsertStatement);

            prepareStatement(childInsertStatement, item);
            childInsertStatement.executeUpdate();

            try (ResultSet generatedKeys = childInsertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    String childIdValue = generatedKeys.getString(1);
                    setIdStatement.setString(1, parentIdValue);
                    setIdStatement.setString(2, childIdValue);
                } else {
                    throw new SQLException("Creating " + tableName + " failed, no ID obtained.");
                }
            }
            session.closeStatement(childInsertStatement);

            setIdStatement.executeUpdate();
            session.closeStatement(setIdStatement);

            session.commit();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Can not insert " + tableName + ": " + e.getMessage());
            System.err.print("Transaction is being rolled back");
            session.rollback();
        } finally {
            session.setAutoCommit(true);
        }
        return true;
    }

    /**
     * Returns object from result set
     *
     * @param resultSet result set
     * @return result object ( object should be instance of E )
     * @throws IllegalAccessException if cannot write into object`s field
     * @throws InstantiationException if cannot create a new instance of target class
     * @throws NoSuchFieldException   cannot found a field
     * @throws SQLException           error with working with work set
     */
    private Object getEntityFromResultSet(ResultSet resultSet) throws IllegalAccessException, InstantiationException, NoSuchFieldException, SQLException {
        Class <?> entityClass = this.getEntityClass();
        String[][] nameMapping = getNameMapping();
        return getEntityFromResultSet(resultSet, entityClass, nameMapping);
    }

    /**
     * Returns object from result set
     *
     * @param resultSet   result set
     * @param entityClass entity class
     * @param nameMapping name mapping
     * @return result object ( object should be instance of E )
     * @throws IllegalAccessException if cannot write into object`s field
     * @throws InstantiationException if cannot create a new instance of target class
     * @throws NoSuchFieldException   cannot found a field
     * @throws SQLException           error with working with work set
     */
    Object getEntityFromResultSet(ResultSet resultSet, Class entityClass, String[][] nameMapping)
            throws IllegalAccessException, InstantiationException, NoSuchFieldException, SQLException {
        Object instance = entityClass.newInstance();
        for (String[] columnFieldPair : (nameMapping)) {
            String fieldName = columnFieldPair[0];
            String columnName = columnFieldPair[1];

            Field field = entityClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            String value = resultSet.getString(columnName);

            setFieldValue(field, instance, value);
        }
        return instance;
    }

    /**
     * Sets field value
     */
    private void setFieldValue(Field field, Object item, String value) throws IllegalAccessException {
        String fieldType = field.getType().getSimpleName();
        switch (fieldType) {
            case "String": {
                field.set(item, value);
                break;
            }
            case "int":
            case "Integer": {
                field.setInt(item, Integer.parseInt(value));
                break;
            }
            case "Double":
            case "double":
            case "Float":
            case "float": {
                field.setFloat(item, Float.parseFloat(value));
                break;
            }
            case "boolean":
            case "Boolean": {
                field.setBoolean(item, Boolean.getBoolean(value));
                break;
            }
            case "Freshness":
            case "FreshnessInteger":
                field.set(item, new FreshnessInteger(Integer.parseInt(value)));
                break;
        }
    }

    /**
     * Sets values in prepare statement
     */
    private PreparedStatement prepareStatement(PreparedStatement statement, E item) throws SQLException, NoSuchFieldException, IllegalAccessException {
        return prepareStatement(statement, getEntityClass(), item, getNameMapping());
    }

    /**
     * Sets values in prepare statement
     */
    private PreparedStatement prepareStatement(PreparedStatement statement, Class entityClass, Object item, String[][] nameMapping) throws SQLException, NoSuchFieldException, IllegalAccessException {
        int parametersCount = statement.getParameterMetaData().getParameterCount();
        for (int i = 0; i < parametersCount; i++) {
            Field field = getField(i, entityClass, nameMapping);
            field.setAccessible(true);
            Object value = field.get(item);
            statement = prepareStatementWithOneValue(statement, entityClass, nameMapping, value, i);
        }
        return statement;
    }

    /**
     * Returns field of target class by index
     * @param index field index in name mapping
     * @return field
     * @throws NoSuchFieldException
     */
    private Field getField(int index) throws NoSuchFieldException {
        return getField(index, getEntityClass(), getNameMapping());

    }

    /**
     * Returns field of target class by index in name mapping
     */
    private Field getField(int index, Class entityClass, String[][] nameMapping) throws NoSuchFieldException {
        String fieldName = nameMapping[index][0];
        return entityClass.getDeclaredField(fieldName);
    }

    /*
     * Sets value in prepare statement by index
     */
    private PreparedStatement prepareStatementWithOneValue(PreparedStatement statement, Object value, int fieldIndex) throws SQLException, NoSuchFieldException, IllegalAccessException {
        return prepareStatementWithOneValue(statement, getEntityClass(), getNameMapping(), value, fieldIndex);
    }

    /*
    * Sets value in prepare statement by index
    */
    private PreparedStatement prepareStatementWithOneValue(PreparedStatement statement, Class entityClass, String[][] nameMapping, Object value, int fieldIndex) throws SQLException, NoSuchFieldException, IllegalAccessException {
        String fieldType = getFieldTypeName(fieldIndex, entityClass, nameMapping);
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

    /**
     * Get field count in name mapping
     */
    private int getFieldCount() {
        return getNameMapping().length;
    }

    /**
     * Get field type by index in mane mapping
     * @param index field index
     * @return field type
     */
    private String getFieldTypeName(int index) throws NoSuchFieldException {
        Field field = getField(index);
        field.setAccessible(true);
        return field.getType().getSimpleName();
    }
    /**
     * Get entity class field type by index in mane mapping
     * @param index field index
     * @param entityClass entity class
     * @param nameMapping name mapping
     * @return field type
     */
    private String getFieldTypeName(int index, Class entityClass, String[][] nameMapping) throws NoSuchFieldException {
        Field field = getField(index, entityClass, nameMapping);
        field.setAccessible(true);
        return field.getType().getSimpleName();
    }

    /**
     * Make arguments for formatted queries
     * @param count arguments count
     * @return arguments as array
     */
    private Object[] makeFormatArgs(int count) {
        String tableName = this.tableName;
        Class entityClass = getEntityClass();
        String[][] nameMapping = getNameMapping();
        return makeFormatArgs(count, tableName, nameMapping);
    }

    /**
     * Make arguments for formatted queries
     * @param count arguments count
     * @param tableName target table name
     * @param nameMapping name mapping for target class
     * @return arguments as array
     */
    private Object[] makeFormatArgs(int count, String tableName, String[][] nameMapping) {
        List <String> args = new ArrayList <>();
        args.add(schemaName);
        args.add(tableName);
        for (int i = 0; i < count; i++) {
            args.add(nameMapping[i][1]);
        }
        return args.toArray();
    }

    /**
     * delete all records from target table
     */
    void truncateTable() {
        truncateTable(this.tableName);
    }

    /**
     * delete all records from table by name
     */
    private void truncateTable(String tableName) {
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
