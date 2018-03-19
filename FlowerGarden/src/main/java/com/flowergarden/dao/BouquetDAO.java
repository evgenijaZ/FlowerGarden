package com.flowergarden.dao;

import com.flowergarden.bouquet.GeneralBouquet;
import com.flowergarden.bouquet.MarriedBouquet;

/**
 * @author Yevheniia Zubrych on 12.03.2018.
 */
public class BouquetDAO extends DAO<MarriedBouquet,Integer> {

    String UPDATE = "UPDATE %s.%s SET %s=? WHERE %s=?;";
    private String INSERT = "INSERT INTO %s.%s (%s) VALUES (?);";
    private String INSERT_BY_ID = "INSERT INTO %s.%s (%s, %s) VALUES (?, ?);";

    private String[][] nameMapping = {{"assemblePrice","assemble_price"},{"id","id"}};

    public BouquetDAO(String dbName, String schemaName, String tableName) {
        super(dbName, schemaName, tableName);
    }

    @Override
    protected Class <MarriedBouquet> getEntityClass() {
        return MarriedBouquet.class;
    }

    @Override
    protected Class <Integer> getKeyClass() {
        return Integer.TYPE;
    }

    @Override
    public String[][] getNameMapping() {
        return nameMapping;
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
