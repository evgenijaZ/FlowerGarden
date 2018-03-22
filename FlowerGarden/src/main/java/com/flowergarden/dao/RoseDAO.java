package com.flowergarden.dao;

import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;

/**
 * @author Yevheniia Zubrych on 23.03.2018.
 */
public class RoseDAO extends FlowerDAO {
    private String[][] nameMapping = {{"spike", "spike"}, {"id", "id"}};
    private String parentTableName;
    private String foreignKey = "flower_id";

    public RoseDAO(String dbName, String schemaName, String parentTableName, String tableName) {
        super(dbName, schemaName, tableName);
        this.parentTableName = parentTableName;
    }

    @Override
    public String[][] getNameMapping() {
        return nameMapping;
    }

    @SuppressWarnings("unchecked")
    protected Class getEntityClass() {
        return Rose.class;
    }

    @Override
    String getSelectAllQuery() {
        String[][] parentMapping = super.getNameMapping();
        return getSelectAllQueryWithParent(parentMapping, parentTableName, foreignKey);
    }

    public boolean create(Rose item) {
        GeneralFlower flower = new GeneralFlower(item.getPrice(), item.getLength(), item.getFreshness());
        return createWithParent(item, parentTableName, super.getNameMapping(), super.getEntityClass(), flower, foreignKey);
    }
}

