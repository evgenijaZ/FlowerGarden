package com.flowergarden.dao;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;
import com.flowergarden.properties.FreshnessInteger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public List <GeneralFlower> getAll() {
        List <GeneralFlower> flowers = null;
        final String query = "SELECT * FROM `flower`";
        ResultSet resultSet;
        try {
            resultSet = handler.executeQuery(query);
            if (resultSet != null) {
                flowers = parseResultSet(resultSet);
                return flowers;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flowers;
    }

    @Override
    public boolean update(GeneralFlower entity) {
        String query;
        int id = entity.getId();
        float price = entity.getPrice();
        int length = entity.getLength();
        if (entity.getClass().equals(Chamomile.class)) {
            int petals = ((Chamomile) entity).getPetals();
            query = String.format("UPDATE `flower` SET `name` = `%s`, `price`= '%5.2f', `length`='%d', `petals`='%d' WHERE `id`='%d'", "chamomile", price, length, petals, id);

        } else if (entity.getClass().equals(Rose.class)) {
            boolean spike = ((Rose) entity).getSpike();
            query = String.format("UPDATE `flower` SET `name` = `%s`, `price`= '%5.2f', `length`='%d', `spike`='%b' WHERE `id`='%d'", "rose", price, length, spike, id);

        } else {
            throw new RuntimeException("This type of flower is not supported :" + entity.getClass().getSimpleName());
        }
        return handler.execute(query);
    }

    @Override
    public GeneralFlower getByKey(Integer key) {
        final String query = "SELECT * FROM `flower` WHERE `id`=" + key;
        List <GeneralFlower> flowers;
        GeneralFlower flower = null;
        ResultSet resultSet;
        try {
            resultSet = handler.executeQuery(query);
            if (resultSet != null) {
                flowers = parseResultSet(resultSet);
                if (flowers.size() == 1)
                    flower = flowers.get(0);
                else throw new RuntimeException("Too many results");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flower;
    }

    public List <GeneralFlower> getByBouquetId(Integer id) {
        List <GeneralFlower> flowers = null;
        final String query = "SELECT * FROM `flower` WHERE `bouquet_id`=" + id;
        ResultSet resultSet;
        try {
            resultSet = handler.executeQuery(query);
            if (resultSet != null) {
                flowers = parseResultSet(resultSet);
                return flowers;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flowers;
    }

    @Override
    public boolean delete(GeneralFlower entity) {
        final String query = "DELETE FROM `flower` WHERE `id`=" + entity.getId();
        return handler.execute(query);
    }

    @Override
    public boolean deleteByKey(Integer key) {
        final String query = "DELETE FROM `flower` WHERE `id`=" + key;
        return handler.execute(query);
    }

    @Override
    public boolean create(GeneralFlower entity) {
        String query;
        int id = entity.getId();
        String name;
        int length = entity.getLength();
        int freshness = entity.getFreshness().getFreshness();
        float price = entity.getPrice();
        if (entity.getClass().equals(Chamomile.class)) {
            name = "chamomile";
            int petals = ((Chamomile) entity).getPetals();
            query = String.format("INSERT INTO`bouquet` (`id`, `name`, `length`, `freshness`, `price`, `petals`) VALUES ('%d', '%s', '%d', '%d', '%5.2f', '%d', '%d')", id, name, length, freshness, price, petals);
        } else if (entity.getClass().equals(Rose.class)) {
            name = "rose";
            boolean spike = ((Rose) entity).getSpike();
            query = String.format("INSERT INTO`bouquet` (`id`, `name`, `length`, `freshness`, `price`, `spike`) VALUES ('%d', '%s', '%d', '%d', '%5.2f', '%d', '%b')", id, name, length, freshness, price, spike);
        } else {
            name = "general";
            query = String.format("INSERT INTO`bouquet` (`id`, `name`, `length`, `freshness`, `price`, `bouquet_id`) VALUES ('%d', '%s', '%d', '%d', '%5.2f', '%d')", id, name, length, freshness, price);
            //throw new RuntimeException("This type of flower is not supported :"+entity.getClass().getSimpleName());
        }
        return handler.execute(query);
    }


    private List <GeneralFlower> parseResultSet(ResultSet resultSet) throws SQLException {
        List <GeneralFlower> flowers = new ArrayList <>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int length = resultSet.getInt("length");
            int freshness = resultSet.getInt("freshness");
            float price = resultSet.getFloat("price");
            int bouquetId = resultSet.getInt("bouquet_id");
            switch (name) {
                case "chamomile": {
                    int petals = resultSet.getInt("petals");
                    Chamomile chamomile = new Chamomile(petals, length, price, new FreshnessInteger(freshness));
                    flowers.add(chamomile);
                    break;
                }
                case "rose": {
                    boolean spike = resultSet.getBoolean("spike");
                    Rose rose = new Rose(spike, length, price, new FreshnessInteger(freshness));
                    flowers.add(rose);
                    break;
                }
                default:
                    throw new RuntimeException("This type of flower is not supported :" + name);
            }
        }
        return flowers;
    }
}
