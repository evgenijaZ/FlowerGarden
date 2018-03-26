package com.flowergarden.dao;

import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 12.03.2018.
 */
public class BouquetDAO extends DAO <MarriedBouquet, Integer> {

    private String[][] nameMapping = {{"assemblePrice", "assemble_price"}, {"id", "id"}};

    public BouquetDAO(DataSource dataSource, String schemaName, String tableName) {
        super(dataSource, schemaName, tableName);
    }

    @Override
    protected Class <MarriedBouquet> getEntityClass() {
        return MarriedBouquet.class;
    }

    @Override
    protected Class <Integer> getKeyClass() {
        return Integer.class;
    }

    @Override
    public String[][] getNameMapping() {
        return nameMapping;
    }

    @Override
    public boolean deleteByKey(Integer key) {
        //TODO: set bouquet_id = null in flowers (or delete)
        return super.deleteByKey(key);
    }

    @Override
    public boolean update(MarriedBouquet entity) {
        //TODO: add updating in flowers
        return super.update(entity);
    }

    @Override
    public MarriedBouquet getByKey(Integer key) {
        MarriedBouquet bouquet = super.getByKey(key);
        if (bouquet == null) return null;
        fillWithFlowers(bouquet, key);
        return bouquet;
    }

    @Override
    public List <MarriedBouquet> getAll() {
        List <MarriedBouquet> bouquets = super.getAll();
        for (MarriedBouquet bouquet : bouquets)
            fillWithFlowers(bouquet, bouquet.getId());
        return bouquets;
    }

    @Override
    public boolean create(MarriedBouquet entity) {
        boolean result = super.create(entity);
        List <GeneralFlower> flowers = (List <GeneralFlower>) entity.getFlowers();
        if (flowers == null || flowers.size() == 0) return result;

        ChamomileDAO chamomileDAO = null;
        RoseDAO roseDAO = null;
        FlowerDAO flowerDAO = null;

        for (GeneralFlower flower : flowers) {
            if (flower instanceof Chamomile) {
                if (chamomileDAO == null)
                    chamomileDAO = new ChamomileDAO(this.dataSource, this.schemaName, "flower", "chamomile");
                chamomileDAO.create(flower);
            } else if (flower instanceof Rose) {
                if (roseDAO == null)
                    roseDAO = new RoseDAO(this.dataSource, this.schemaName, "flower", "rose");
                roseDAO.create(flower);
            } else {
                if (flowerDAO == null)
                    flowerDAO = new FlowerDAO(this.dataSource, this.schemaName, "flower");
                flowerDAO.create(flower);
            }
        }
        setBouquetId(entity);
        return result;
    }

    private void fillWithFlowers(MarriedBouquet bouquet, int key) {
        if (bouquet == null) return;
        String selectChamomilesQuery = "SELECT * FROM (SELECT flower.id, flower.price, flower.length, flower.freshness, chamomile.petals, flower.bouquet_id from flower JOIN chamomile on flower.id = chamomile.flower_id) WHERE bouquet_id = " + key;
        String selectRosesQuery = "SELECT * FROM (SELECT flower.id, flower.price, flower.length, flower.freshness, rose.spike, flower.bouquet_id from flower JOIN rose on flower.id = rose.flower_id) WHERE bouquet_id = " + key;

        String[][] chamomileMapping = ChamomileDAO.nameMapping;
        String[][] roseMapping = RoseDAO.nameMapping;

        String[][] flowerMapping = FlowerDAO.nameMapping;

        PreparedStatement selectChamomilesStatement = session.getPrepareStatement(selectChamomilesQuery);
        try {
            ResultSet chamomileResultSet = selectChamomilesStatement.executeQuery();
            while (chamomileResultSet.next()) {
                GeneralFlower flower = (GeneralFlower) getEntityFromResultSet(chamomileResultSet, GeneralFlower.class, flowerMapping);
                Chamomile resultChamomile = (Chamomile) getEntityFromResultSet(chamomileResultSet, Chamomile.class, chamomileMapping);
                if (resultChamomile != null && flower != null) {
                    resultChamomile.setPrice(flower.getPrice());
                    resultChamomile.setLength(flower.getLength());
                    resultChamomile.setFreshness(flower.getFreshness());
                    bouquet.addFlower(resultChamomile);
                }
            }
            session.closeStatement(selectChamomilesStatement);

        } catch (IllegalAccessException | InstantiationException | NoSuchFieldException | SQLException e) {
            System.err.println("Can not get chamomiles result set from the query:" + selectChamomilesQuery + " " + e.getMessage());
        }


        PreparedStatement selectRosesStatement = session.getPrepareStatement(selectRosesQuery);
        try {
            ResultSet roseResultSet = selectRosesStatement.executeQuery();
            while (roseResultSet.next()) {
                GeneralFlower flower = (GeneralFlower) getEntityFromResultSet(roseResultSet, GeneralFlower.class, flowerMapping);
                Rose resultRose = (Rose) getEntityFromResultSet(roseResultSet, Rose.class, roseMapping);
                if (resultRose != null) {
                    resultRose.setFreshness(flower.getFreshness());
                    resultRose.setPrice(flower.getPrice());
                    resultRose.setLength(flower.getLength());
                    bouquet.addFlower(resultRose);
                }
            }
            session.closeStatement(selectRosesStatement);

        } catch (IllegalAccessException | InstantiationException | NoSuchFieldException | SQLException e) {
            System.err.println("Can not get roses result set from the query:" + selectChamomilesQuery + " " + e.getMessage());
        }
    }

    private void setBouquetId(MarriedBouquet bouquet) {
        List <GeneralFlower> flowers = (List <GeneralFlower>) bouquet.getFlowers();
        if (flowers == null) return;
        int bouquetId = bouquet.getId();
        String query = "update flower set bouquet_id = ? WHERE id = ?";

        PreparedStatement statement = session.getPrepareStatement(query);

        try {
            for (GeneralFlower flower : flowers) {
                statement.setInt(1, bouquetId);
                statement.setInt(2, flower.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            System.err.println("Cannot update bouquet_id values: " + e.getMessage());
        }
        session.closeStatement(statement);

    }
}
