package com.flowergarden.dao;

import com.flowergarden.flowers.GeneralFlower;

import java.util.List;

/**
 * @author Yevheniia Zubrych on 13.03.2018.
 */
public class FlowerDAO extends AbstractDAO <GeneralFlower, Integer> {

    private DataBaseHandler handler;

    public FlowerDAO(DataBaseHandler handler) {
        this.handler = handler;
    }

    public FlowerDAO() {
        this.handler = new DataBaseHandler();
    }

    @Override
    public List<GeneralFlower> getAll() {
        return null;
    }

    @Override
    public boolean update(GeneralFlower entity) {
        return false;
    }

    @Override
    public GeneralFlower getByKey(Integer key) {
        return null;
    }

    @Override
    public boolean delete(GeneralFlower entity) {
        return false;
    }

    @Override
    public boolean deleteByKey(Integer key) {
        return false;
    }

    @Override
    public boolean create(GeneralFlower entity) {
        return false;
    }
}
