package com.flowergarden.dao;

import com.flowergarden.flowers.GeneralFlower;

/**
 * @author Yevheniia Zubrych on 13.03.2018.
 */
public class FlowerDAO extends DAO <GeneralFlower, Integer> {
    String UPDATE = "UPDATE %s.%s SET %s=?,%s=?,%s=? WHERE %s=?;";
    private String INSERT = "INSERT INTO %s.%s (%s, %s, %s) VALUES (?, ?, ?);";
    private String INSERT_BY_ID = "INSERT INTO %s.%s (%s, %s, %s, %s) VALUES (?, ?, ?, ?);";

    private String[][] nameMapping = {{"freshness", "freshness"}, {"length", "length"}, {"price", "price"}, {"id", "id"}};

    public FlowerDAO(String dbName, String schemaName, String tableName) {
        super(dbName, schemaName, tableName);
    }

    @Override
    protected Class <GeneralFlower> getEntityClass() {
        return GeneralFlower.class;
    }

    @Override
    protected Class <Integer> getKeyClass() {
        return Integer.class;
    }

    @Override
    public String[][] getNameMapping() {
        return this.nameMapping;
    }

    @Override
    String getUpdateQuery() {
        return String.format(this.UPDATE, makeFormatArgs(getFieldCount(), getNameMapping()));
    }

    @Override
    public String getInsertQuery() {
        return String.format(INSERT, makeFormatArgs(getFieldCount() - 1, getNameMapping()));

    }

    @Override
    public String getInsertByIdQuery() {
        return String.format(INSERT_BY_ID, makeFormatArgs(getFieldCount(), getNameMapping()));
    }

}
