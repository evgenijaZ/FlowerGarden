package com.flowergarden.dao;

import com.flowergarden.flowers.GeneralFlower;

import java.util.List;

/**
 * @author Yevheniia Zubrych on 13.03.2018.
 */
public class FlowerDAO extends DAO <GeneralFlower, Integer> {
    String UPDATE = "UPDATE %s.%s SET %s=?,%s=?,%s=? WHERE %s=?;";

    private String[][] nameMapping = {{"freshness", "freshness"}, {"length", "length"}, {"price", "price"}, {"id", "id"}};

    public FlowerDAO(String dbName, String schemaName, String tableName) {
        super(dbName,schemaName, tableName);
    }

    @Override
    protected Class <GeneralFlower> getEntityClass() {
        return GeneralFlower.class;
    }

    @Override
    protected Class <Integer> getKeyClass() {
        return Integer.TYPE;
    }

    @Override
    public String[][] getNameMapping() {
        return this.nameMapping;
    }

    @Override
    String getUpdateQuery() {
        return String.format(this.UPDATE, makeFormatArgs(getFieldCount(), getNameMapping()));
    }
}
