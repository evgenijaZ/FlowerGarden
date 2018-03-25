package com.flowergarden.dao;

import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.Rose;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.naming.NamingException;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 21.03.2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class BouquetDAOTest {
    @Autowired
    ApplicationContext context;
    private BouquetDAO dao;

    private MarriedBouquet bouquet1;
    private MarriedBouquet bouquet2;
    private MarriedBouquet bouquet3;

    public BouquetDAOTest() throws NamingException {
    }

    @Before
    public void initDao() {
        dao = (BouquetDAO) context.getBean("bouquetDAO");
        dao.truncateTable();
    }

    @Before
    public void initBouquets() {
        bouquet1 = (MarriedBouquet) context.getBean("marriedBouquet1");
        bouquet2 = (MarriedBouquet) context.getBean("marriedBouquet2");
        bouquet3 = (MarriedBouquet) context.getBean("marriedBouquet3");
    }

    @Test
    public void testGettingListOfBouquets() {
        //Given
        //dao.create(bouquet1);
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
    public void testPrice() {
        //Given
        Chamomile chamomile1 = (Chamomile) context.getBean("chamomile1");
        Chamomile chamomile2 = (Chamomile) context.getBean("chamomile2");

        Rose rose = (Rose) context.getBean("rose1");

        bouquet1.addFlower(chamomile1);
        bouquet1.addFlower(chamomile2);
        bouquet1.addFlower(rose);

        dao.create(bouquet1);
        int bouquetId = bouquet1.getId();

        //When
        MarriedBouquet actualBouquet = dao.getByKey(bouquetId);

        //Then
        Assert.assertEquals(bouquet1.getPrice(), actualBouquet.getPrice(), 0.001);

        bouquet1=null;
        bouquet2=null;
        bouquet3=null;
    }

    @After
    public void cleanTable() {
         dao.truncateTable();
    }
}