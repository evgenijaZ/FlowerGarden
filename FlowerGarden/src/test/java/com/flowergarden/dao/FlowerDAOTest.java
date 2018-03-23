package com.flowergarden.dao;

import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.properties.FreshnessInteger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Yevheniia Zubrych on 19.03.2018.
 */
public class FlowerDAOTest {
    private DAO <GeneralFlower, Integer> dao;

    private GeneralFlower flower1;
    private GeneralFlower flower2;
    private GeneralFlower flower3;
    private GeneralFlower flower4;

    @Before
    public void initDao() {
        dao = new FlowerDAO("flowergarden", "main", "flower");
        dao.truncateTable();
    }

    @Before
    public void initFlowers() {
        flower1 = new GeneralFlower(15, 16, new FreshnessInteger(5));
        flower2 = new GeneralFlower(13.5f, 13, new FreshnessInteger(4));
        flower3 = new GeneralFlower(14, 10, new FreshnessInteger(4));
        flower4 = new GeneralFlower(14.3f, 15, new FreshnessInteger(6));
    }

    @Test
    public void testGettingListOfFlowers() throws Exception {
        //Given
        dao.create(flower1);
        dao.create(flower2);
        dao.create(flower3);
        dao.create(flower4);

        //When
        List <GeneralFlower> flowers = dao.getAll();

        //Then
        Assert.assertNotNull(flowers);
    }

    @Test
    public void testSizeOfReceivedFlowerList() {
        //Given
        dao.create(flower1);
        dao.create(flower2);
        dao.create(flower3);
        dao.create(flower4);

        //When
        List <GeneralFlower> flowers = dao.getAll();

        //Then
        Assert.assertEquals(4, flowers.size());
    }

    @Test
    public void testUpdatingFlowerFreshness() {
        //Given
        dao.create(flower1);

        //When
        FreshnessInteger freshness = new FreshnessInteger(2);
        flower1.setFreshness(freshness);

        dao.update(flower1);

        //Then
        Assert.assertEquals(freshness, dao.getByKey(flower1.getId()).getFreshness());
    }

    @Test
    public void getByKey() {
        //Given
        dao.create(flower1);
        int id = flower1.getId();

        //When
        GeneralFlower actualFlower = dao.getByKey(id);

        //Then
        Assert.assertEquals(flower1,actualFlower);
    }

    @Test
    public void testDeletingByKey() {
        //Given
        dao.create(flower4);
        int id = flower4.getId();

        //When
        dao.deleteByKey(id);

        //Then
        Assert.assertEquals(null, dao.getByKey(id));
    }

    @Test
    public void testInserting() {
        //When
        dao.create(flower2);
        int newId = flower2.getId();

        //Then
        Assert.assertEquals(flower2, dao.getByKey(newId));
    }

    @Test
    public void testInsertingById() {
        //Given
        int id = 34;
        flower2.setId(id);

        //When
        dao.create(flower2);

        //Then
        GeneralFlower actualFlower =  dao.getByKey(id);
        Assert.assertEquals(flower2,actualFlower);
    }

    @After
    public void cleanTable() {
        dao.truncateTable();
    }

}