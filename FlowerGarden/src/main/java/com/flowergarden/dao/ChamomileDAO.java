package com.flowergarden.dao;

import com.flowergarden.flowers.Chamomile;

/**
 * @author Yevheniia Zubrych on 18.03.2018.
 */
public class ChamomileDAO extends FlowerDAO {
    String UPDATE = "UPDATE %s.%s SET %s=? WHERE %s=?;";
    private String INSERT = "INSERT INTO %s.%s (%s) VALUES (?);";
    private String INSERT_BY_ID = "INSERT INTO %s.%s (%s, %s) VALUES (?, ?);";

    private String[][] nameMapping = {{"petals", "petals"}, {"id", "id"}};

    private String parentTableName;

    public ChamomileDAO(String dbName, String schemaName, String parentTableName, String tableName) {
        super(dbName, schemaName, tableName);
        this.parentTableName = parentTableName;
    }

    @Override
    public String[][] getNameMapping() {
        return nameMapping;
    }

    @SuppressWarnings("unchecked")
    protected Class getEntityClass() {
        return Chamomile.class;
    }

    @Override
    String getSelectAllQuery() {
        StringBuilder selectAllQuery = new StringBuilder("SELECT ");
        String[][] parentMapping = super.getNameMapping();
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
        selectAllQuery.append("FROM ").append(tableName)
                .append(" LEFT JOIN ").append(parentTableName)
                .append(" ON  ").append(tableName).append(".flower_id=").append(parentTableName).append(".id;");
        return selectAllQuery.toString();
    }


    @Override
    String getUpdateQuery() {
        //to do: добавить изменение в родительской таблице по запросу
        return String.format(this.UPDATE, makeFormatArgs(getFieldCount(), getNameMapping()));
    }

    public String getInsertQuery() {
        return String.format(INSERT, makeFormatArgs(getFieldCount() - 1, getNameMapping()));

    }

    @Override
    public String getInsertByIdQuery() {
        return String.format(INSERT_BY_ID, makeFormatArgs(getFieldCount(), getNameMapping()));
    }

}
