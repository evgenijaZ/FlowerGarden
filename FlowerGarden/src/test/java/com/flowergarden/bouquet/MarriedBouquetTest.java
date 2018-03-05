package com.flowergarden.bouquet;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;
import com.flowergarden.flowers.Tulip;
import com.flowergarden.properties.FreshnessInteger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 04.03.2018.
 */
public class MarriedBouquetTest {

    private MarriedBouquet bouquet;
    private int chamomilePrice = 15;
    private int rosePrice = 35;

    @Before
    public void initBouquet() {
        //Given
        bouquet = new MarriedBouquet();
        bouquet.addFlower(new Chamomile(12, 25, chamomilePrice, new FreshnessInteger(9)));
        bouquet.addFlower(new Rose(true, 50, rosePrice, new FreshnessInteger(10)));
        bouquet.addFlower(new Rose(false, 45, rosePrice, new FreshnessInteger(8)));
    }

    @Test
    public void testGettingPrice() throws Exception {
        float expectedPrice = chamomilePrice + 2 * rosePrice + bouquet.getAssemblePrice();
        Assert.assertEquals(expectedPrice, bouquet.getPrice(), 0.001);
    }

    @Test
    public void testEmptySearchResult() throws Exception {
        int start = 70;
        int end = 100;
        //When
        List <GeneralFlower> searchResult = new ArrayList <>(bouquet.searchFlowersByLength(start, end));
        //Then
        Assert.assertEquals(0, searchResult.size());
    }

    @Test
    public void testSizeOfSearchResultsByLength() throws Exception {
        int start = 40;
        int end = 50;
        //When
        List <GeneralFlower> searchResult = new ArrayList <>(bouquet.searchFlowersByLength(start, end));
        //Then
        Assert.assertEquals(2, searchResult.size());
    }

    @Test
    public void testLengthOfEachElementOfSearchResults() throws Exception {
        int start = 40;
        int end = 50;
        //When
        List <GeneralFlower> searchResult = new ArrayList <>(bouquet.searchFlowersByLength(start, end));
        for (GeneralFlower flower : searchResult) {
            int flowerLength = flower.getLength();
            //Then
            Assert.assertTrue(start <= flowerLength && flowerLength <= end);
        }
    }


    @Test(expected = NullPointerException.class)
    public void testHandlingNullPointerException() throws Exception {
        //Given
        bouquet = null;
        //When
        bouquet.sortByFreshness();
    }

    @Test
    public void testFlowerList(){
    }

}