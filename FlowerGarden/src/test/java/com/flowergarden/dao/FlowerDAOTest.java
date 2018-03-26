package com.flowergarden.dao;

import com.flowergarden.context.ApplicationContextDAO;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.properties.FreshnessInteger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

/**
 * @author Yevheniia Zubrych on 19.03.2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class FlowerDAOTest {

    private ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextDAO.class);

    private FlowerDAO dao;

    private GeneralFlower flower1;
    private GeneralFlower flower2;
    private GeneralFlower flower3;
    private GeneralFlower flower4;

    @Before
    public void initDao() {
        dao = (FlowerDAO) context.getBean("flowerDAO");
        dao.truncateTable();
    }

    @Before
    public void initFlowers() {
        flower1 = (GeneralFlower) context.getBean("flower1");
        flower2 = (GeneralFlower) context.getBean("flower2");
        flower3 = (GeneralFlower) context.getBean("flower3");
        flower4 = (GeneralFlower) context.getBean("flower4");
    }

    @Test
    public void testGettingListOfFlowers() {
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
        Assert.assertEquals(flower1, actualFlower);
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
        GeneralFlower actualFlower = dao.getByKey(id);
        Assert.assertEquals(flower2, actualFlower);
    }

    @After
    public void cleanTable() {
        dao.truncateTable();
    }

    @Configuration
    public static class ContextConfiguration {
    }
}