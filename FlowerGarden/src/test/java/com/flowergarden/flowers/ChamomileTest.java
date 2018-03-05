package com.flowergarden.flowers;

import com.flowergarden.properties.FreshnessInteger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Yevheniia Zubrych on 04.03.2018.
 */

public class ChamomileTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FreshnessInteger freshness;

    private Chamomile chamomile;

    @Test
    public void testGettingPetal() throws Exception {
        //Given
        chamomile = new Chamomile(2, 20, 5, freshness);
        Assert.assertEquals(true, chamomile.getPetal());
    }

    @Test
    public void testGettingTheLastPetal() throws Exception {
        //Given
        chamomile = new Chamomile(0, 20, 5, freshness);
        //When
        boolean petalIsObtained = chamomile.getPetal();
        //Then
        Assert.assertEquals(false, petalIsObtained);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePetals() throws Exception {
        //Given
        chamomile = new Chamomile(-1, 20, 5, freshness);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() throws Exception {
        //Given
        chamomile = new Chamomile(10, -20, 5, freshness);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePrice() throws Exception {
        //Given
        chamomile = new Chamomile(10, 20, -5, freshness);
    }
}