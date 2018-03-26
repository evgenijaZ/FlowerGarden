package com.flowergarden.dao;

import com.flowergarden.flowers.GeneralFlower;

import javax.sql.DataSource;

/**
 * @author Yevheniia Zubrych on 13.03.2018.
 */
public class FlowerDAO extends DAO <GeneralFlower, Integer> {

    static String[][] nameMapping = {{"freshness", "freshness"}, {"length", "length"}, {"price", "price"}, {"id", "id"}};

    public FlowerDAO(DataSource dataSource, String schemaName, String tableName) {
        super(dataSource, schemaName, tableName);
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
        return nameMapping;
    }

}
