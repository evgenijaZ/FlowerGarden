package com.flowergarden.bouquet;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.Rose;
import com.flowergarden.properties.FreshnessInteger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Yevheniia Zubrych on 04.03.2018.
 */
public class MarriedBouquetTest {

    private MarriedBouquet bouquet;

    private int chamomilePrice = 15;
    private int rosePrice = 35;
    private int assembledPrice = 150;

    @Before
    public void initBouquet() {
        bouquet = new MarriedBouquet();
        bouquet.addFlower(new Chamomile(12, 25, chamomilePrice, new FreshnessInteger(9)));
        bouquet.addFlower(new Rose(true, 50, rosePrice, new FreshnessInteger(10)));
        bouquet.addFlower(new Rose(false, 45, rosePrice, new FreshnessInteger(8)));
        bouquet.setAssembledPrice(assembledPrice);
    }

    @Test
    public void getPriceTest() throws Exception {
        int expectedPrice = chamomilePrice + 2 * rosePrice + assembledPrice;
        Assert.assertEquals(expectedPrice, bouquet.getPrice(), 0.001);
    }

    @Test
    public void searchFlowersByLength() throws Exception {
    }

    @Test
    public void sortByFreshness() throws Exception {
    }

}