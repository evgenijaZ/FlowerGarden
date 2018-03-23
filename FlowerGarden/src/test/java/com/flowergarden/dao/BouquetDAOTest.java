package com.flowergarden.dao;

import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.Rose;
import com.flowergarden.properties.FreshnessInteger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Yevheniia Zubrych on 21.03.2018.
 */
public class BouquetDAOTest {
    private BouquetDAO dao;

    private MarriedBouquet bouquet1;
    private MarriedBouquet bouquet2;
    private MarriedBouquet bouquet3;

    @Before
    public void initDao() {
        dao = new BouquetDAO("flowergarden", "main", "married_bouquet");
        dao.truncateTable();
    }

    @Before
    public void initBouquets() {
        bouquet1 = new MarriedBouquet(100);
        bouquet2 = new MarriedBouquet(140);
        bouquet3 = new MarriedBouquet();
    }

    @Test
    public void testGettingListOfBouquets() throws Exception {
        //Given
        dao.create(bouquet1);
        dao.create(bouquet2);
        dao.create(bouquet3);

        //When
        List <MarriedBouquet> bouquets = dao.getAll();

        //Then
        Assert.assertNotNull(bouquets);
    }

    @Test
    public void testSizeOfReceivedFlowerList() {
        //Given
        dao.create(bouquet1);
        dao.create(bouquet2);
        dao.create(bouquet3);

        //When
        List <MarriedBouquet> bouquets = dao.getAll();

        //Then
        Assert.assertEquals(3, bouquets.size());
    }

    @Test
    public void testUpdatingFlowerFreshness() {
        //Given
        dao.create(bouquet1);

        //When
        float newAssemblePrice = 95;
        bouquet1.setAssembledPrice(newAssemblePrice);

        dao.update(bouquet1);

        //Then
        Assert.assertEquals(newAssemblePrice, dao.getByKey(bouquet1.getId()).getAssemblePrice(), 0.001);
    }

    @Test
    public void getByKey() {
        //Given
        dao.create(bouquet2);
        int id = bouquet2.getId();

        //When
        MarriedBouquet actualBouquet = dao.getByKey(id);

        //Then
        Assert.assertEquals(bouquet2, actualBouquet);
    }

    @Test
    public void testDeletingByKey() {
        //Given
        dao.create(bouquet3);
        int id = bouquet3.getId();

        //When
        dao.deleteByKey(id);

        //Then
        Assert.assertEquals(null, dao.getByKey(id));
    }

    @Test
    public void testInserting() {
        //When
        dao.create(bouquet3);
        int newId = bouquet3.getId();

        //Then
        Assert.assertEquals(bouquet3, dao.getByKey(newId));
    }

    @Test
    public void testInsertingById() {
        //Given
        int id = 34;
        bouquet2.setId(id);

        //When
        dao.create(bouquet2);

        //Then
        MarriedBouquet actualBouquet = dao.getByKey(id);
        Assert.assertEquals(bouquet2, actualBouquet);
    }

    @Test
    public void testPrice(){
        //Given
        bouquet1.setId(4);

        Chamomile chamomile1 = new Chamomile(24, 12, 25, new FreshnessInteger(4));
        Chamomile chamomile2 = new Chamomile(24, 16, 50, new FreshnessInteger(5));

        Rose rose = new Rose(false, 212, 285, new FreshnessInteger(434));

        bouquet1.addFlower(chamomile1);
        bouquet1.addFlower(chamomile2);
        bouquet1.addFlower(rose);

        dao.create(bouquet1);

        Assert.assertEquals(25+50+285+100, bouquet1.getPrice(), 0.001);
    }

    @After
    public void cleanTable() {
       // dao.truncateTable();
    }
}