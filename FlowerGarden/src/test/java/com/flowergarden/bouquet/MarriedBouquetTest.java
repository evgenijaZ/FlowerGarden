package com.flowergarden.bouquet;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;
import com.flowergarden.properties.FreshnessInteger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author Yevheniia Zubrych on 04.03.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class MarriedBouquetTest {

    private MarriedBouquet bouquet;
    private float chamomilePrice = 15f;
    private float rosePrice = 35f;
    @Mock
    private Chamomile chamomile;
    @Mock
    private Rose roseWithSpikes;
    @Mock
    private Rose roseWithoutSpikes;

    @Before
    public void initBouquet() {
        //Given
        bouquet = new MarriedBouquet();

        // when
        when(chamomile.getPrice()).thenReturn(chamomilePrice);
        when(chamomile.getLength()).thenReturn(25);

        // when(roseWithSpikes.getSpike()).thenReturn(true);
        when(roseWithSpikes.getLength()).thenReturn(50);
        when(roseWithSpikes.getPrice()).thenReturn(rosePrice);

        // when(roseWithoutSpikes.getSpike()).thenReturn(false);
        when(roseWithoutSpikes.getLength()).thenReturn(45);
        when(roseWithoutSpikes.getPrice()).thenReturn(rosePrice);

        bouquet.addFlower(chamomile);
        bouquet.addFlower(roseWithSpikes);
        bouquet.addFlower(roseWithoutSpikes);
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

    public void testWrongSearchParameters() throws Exception {
        //When
        List <GeneralFlower> searchResult = new ArrayList <>(bouquet.searchFlowersByLength(1000, -10));
        //Then
        Assert.assertEquals(0, searchResult.size());
    }
}