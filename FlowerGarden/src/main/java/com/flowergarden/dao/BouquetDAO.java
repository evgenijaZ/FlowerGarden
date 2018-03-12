package com.flowergarden.dao;

import com.flowergarden.bouquet.Bouquet;
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
        List <GeneralBouquet> bouquets = new ArrayList <>();
        String query = "SELECT * FROM `bouquet`";
        ResultSet resultSet = null;
        try {
            resultSet = handler.executeQuery(query);
            if (resultSet != null) {
                bouquets = parseResultSet(resultSet);
                return bouquets;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GeneralBouquet update(GeneralBouquet entity) {
        return null;
    }

    @Override
    public GeneralBouquet getById(Integer id) {
        String query = "SELECT * FROM `bouquet` WHERE `id`=" + id;
        List <GeneralBouquet> bouquets;
        ResultSet resultSet = null;
        try {
            resultSet = handler.executeQuery(query);
            if (resultSet != null) {
                bouquets = parseResultSet(resultSet);
                if (bouquets.size() == 1)
                    return bouquets.get(0);
                else throw new RuntimeException("Too many results");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(GeneralBouquet entity) {
        return false;
    }

    @Override
    public boolean create(GeneralBouquet entity) {
        return false;
    }

    private List <GeneralBouquet> parseResultSet(ResultSet resultSet) throws SQLException {
        List <GeneralBouquet> bouquets = new ArrayList <>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int assembledPrice = resultSet.getInt("assemble_price");
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
