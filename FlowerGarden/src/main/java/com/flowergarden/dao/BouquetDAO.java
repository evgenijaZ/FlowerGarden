package com.flowergarden.dao;

import com.flowergarden.bouquet.GeneralBouquet;
import com.flowergarden.bouquet.MarriedBouquet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 12.03.2018.
 */
public class BouquetDAO extends AbstractDAO <GeneralBouquet, Integer> {
    private DataBaseHandler handler;

    public BouquetDAO(DataBaseHandler handler) {
        this.handler = handler;
    }

    public BouquetDAO() {
        this.handler = new DataBaseHandler();
    }

    @Override
    public List <GeneralBouquet> getAll() {
        List <GeneralBouquet> bouquets = null;
        final String query = "SELECT * FROM `bouquet`";
        ResultSet resultSet;
        try {
            resultSet = handler.executeQuery(query);
            if (resultSet != null) {
                bouquets = parseResultSet(resultSet);
                return bouquets;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bouquets;
    }

    @Override
    public boolean update(GeneralBouquet entity) {
        String query;
        int id = entity.getId();
        if (entity.getClass().equals(MarriedBouquet.class)) {
            float assemblePrice = ((MarriedBouquet) entity).getAssemblePrice();
            query = String.format("UPDATE `bouquet` SET `name` = `%s`, `assemble_price`= '%5.2f' WHERE `id`='%d'", "married", assemblePrice, id);

        } else {
            throw new RuntimeException("This type of bouquet is not supported :" + entity.getClass().getSimpleName());
        }
        return handler.execute(query);
    }

    @Override
    public GeneralBouquet getByKey(Integer id) {
        final String query = "SELECT * FROM `bouquet` WHERE `id`=" + id;
        List <GeneralBouquet> bouquets;
        GeneralBouquet bouquet = null;
        ResultSet resultSet;
        try {
            resultSet = handler.executeQuery(query);
            if (resultSet != null) {
                bouquets = parseResultSet(resultSet);
                if (bouquets.size() == 1)
                    bouquet = bouquets.get(0);
                else throw new RuntimeException("Too many results");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bouquet;
    }

    @Override
    public boolean delete(GeneralBouquet entity) {
        final String query = "DELETE FROM `bouquet` WHERE `id`=" + entity.getId();
        return handler.execute(query);
    }

    @Override
    public boolean deleteByKey(Integer id) {
        final String query = "DELETE FROM `bouquet` WHERE `id`=" + id;
        return handler.execute(query);
    }

    @Override
    public boolean create(GeneralBouquet entity) {
        String query;
        int id = entity.getId();
        if (entity.getClass().equals(MarriedBouquet.class)) {
            float assemblePrice = ((MarriedBouquet) entity).getAssemblePrice();
            query = String.format("INSERT INTO`bouquet` (`id`, `name`, `assemble_price`) VALUES ('%d', '%s', '%5.2f')", id, "married", assemblePrice);
        } else {
            query = String.format("INSERT INTO`bouquet` (`id`, `name`) VALUES ('%d', '%s')", id, "general");
           //throw new RuntimeException("This type of bouquet is not supported :"+entity.getClass().getSimpleName());
        }
        return handler.execute(query);
    }

    private List <GeneralBouquet> parseResultSet(ResultSet resultSet) throws SQLException {
        List <GeneralBouquet> bouquets = new ArrayList <>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            float assembledPrice = resultSet.getInt("assemble_price");
            if (name.equals("married")) {
                MarriedBouquet bouquet = new MarriedBouquet();
                bouquet.setId(id);
                bouquet.setAssembledPrice(assembledPrice);
                bouquets.add(bouquet);
            } else
                throw new RuntimeException("Cannot get" + name + " bouquet");
        }
        return bouquets;
    }
}
