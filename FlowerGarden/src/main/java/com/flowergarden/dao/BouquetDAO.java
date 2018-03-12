package com.flowergarden.dao;

import com.flowergarden.bouquet.MarriedBouquet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 12.03.2018.
 */
public class BouquetDAO extends AbstractDAO <MarriedBouquet, Integer> {
    private DataBaseHandler handler;

    public BouquetDAO(DataBaseHandler handler) {
        this.handler = handler;
    }

    public BouquetDAO() {
        this.handler = new DataBaseHandler();
    }

    @Override
    public List <MarriedBouquet> getAll() {
        List <MarriedBouquet> bouquets = new ArrayList <>();
        String query = "SELECT * FROM `bouquet`";
        ResultSet resultSet = null;
        try {
            resultSet = handler.executeQuery(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int assembledPrice = resultSet.getInt("assemble_price");
                    MarriedBouquet bouquet = null;
                    if (name.equals("married")) {
                        bouquet = new MarriedBouquet();
                        bouquet.setAssembledPrice(assembledPrice);
                    }
                    if (bouquet != null)
                        bouquets.add(bouquet);
                }
                return bouquets;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MarriedBouquet update(MarriedBouquet entity) {
        return null;
    }

    @Override
    public MarriedBouquet getById(Integer id) {
        return null;
    }

    @Override
    public boolean delete(MarriedBouquet entity) {
        return false;
    }

    @Override
    public boolean create(MarriedBouquet entity) {
        return false;
    }
}
