package com.flowergarden.dao;

import com.flowergarden.bouquet.MarriedBouquet;

/**
 * @author Yevheniia Zubrych on 12.03.2018.
 */
public class BouquetDAO extends DAO <MarriedBouquet, Integer> {

    private String[][] nameMapping = {{"assemblePrice", "assemble_price"}, {"id", "id"}};

    public BouquetDAO(String dbName, String schemaName, String tableName) {
        super(dbName, schemaName, tableName);
    }

    @Override
    protected Class <MarriedBouquet> getEntityClass() {
        return MarriedBouquet.class;
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
