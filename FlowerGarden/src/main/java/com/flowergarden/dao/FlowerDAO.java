package com.flowergarden.dao;

import com.flowergarden.flowers.GeneralFlower;

/**
 * @author Yevheniia Zubrych on 13.03.2018.
 */
public class FlowerDAO extends DAO <GeneralFlower, Integer> {

    private String[][] nameMapping = {{"freshness", "freshness"}, {"length", "length"}, {"price", "price"}, {"id", "id"}};

    FlowerDAO(String dbName, String schemaName, String tableName) {
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

}
