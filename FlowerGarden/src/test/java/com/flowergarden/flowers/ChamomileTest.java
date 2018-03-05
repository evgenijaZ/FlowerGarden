package com.flowergarden.flowers;

import com.flowergarden.properties.FreshnessInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Yevheniia Zubrych on 04.03.2018.
 */
public class ChamomileTest {

    private Chamomile chamomile;

    @Test
    public void testGettingPetal() throws Exception {
        //Given
        chamomile = new Chamomile(2, 20, 5, new FreshnessInteger(5));
        Assert.assertEquals(true, chamomile.getPetal());
    }

    @Test
    public void testGettingTheLastPetal() throws Exception {
        //Given
        chamomile = new Chamomile(0, 20, 5, new FreshnessInteger(5));
        //When
        boolean petalIsObtained = chamomile.getPetal();
        //Then
        Assert.assertEquals(false, petalIsObtained);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePetals() throws Exception {
        //Given
        chamomile = new Chamomile(-1, 20, 5, new FreshnessInteger(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() throws Exception {
        //Given
        chamomile = new Chamomile(10, -20, 5, new FreshnessInteger(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePrice() throws Exception {
        //Given
        chamomile = new Chamomile(10, 20, -5, new FreshnessInteger(5));
    }
}