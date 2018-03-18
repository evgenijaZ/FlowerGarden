package com.flowergarden.dao;

import java.util.List;

/**
 * @author Yevheniia Zubrych on 18.03.2018.
 */
public abstract class DAO<E, K> implements InterfaceDAO <E, K> {

    private Session session;
    String tableName;
    String dbName;

    public DAO(String tableName, String dbName) {
        this.tableName = tableName;
        this.dbName = dbName;
        this.session = new Session(dbName);
    }

    protected abstract Class <E> getEntityClass();

    protected abstract Class <K> getKeyClass();

    public abstract String[][] getNameMapping();

    @Override
    public List <E> getAll() {
        return null;
    }

    @Override
    public boolean update(E entity) {
        return false;
    }

    @Override
    public E getByKey(K key) {
        return null;
    }

    @Override
    public boolean deleteByKey(K key) {
        return false;
    }

    @Override
    public boolean create(E entity) {
        return false;
    }
}
