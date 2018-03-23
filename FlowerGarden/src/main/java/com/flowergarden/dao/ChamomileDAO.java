package com.flowergarden.dao;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;

/**
 * @author Yevheniia Zubrych on 18.03.2018.
 */
public class ChamomileDAO extends FlowerDAO {
    static String[][] nameMapping = {{"petals", "petals"}, {"id", "id"}};
    private String parentTableName;
    private String foreignKey = "flower_id";

    ChamomileDAO(String dbName, String schemaName, String parentTableName, String tableName) {
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
        String[][] parentMapping = super.getNameMapping();
        return getSelectAllQueryWithParent(parentMapping, parentTableName, foreignKey);
    }

    @Override
    public boolean create(GeneralFlower item) {
        GeneralFlower flower = new GeneralFlower(item.getPrice(), item.getLength(), item.getFreshness());
        return createWithParent(item, parentTableName, super.getNameMapping(), super.getEntityClass(), flower, foreignKey);

    }

    @Override
    public boolean update(GeneralFlower entity) {
        //TODO: update flower`s attributes
        return super.update(entity);
    }

    @Override
    public boolean deleteByKey(Integer key) {
        //TODO: delete flower from parent table
        return super.deleteByKey(key);
    }
}
