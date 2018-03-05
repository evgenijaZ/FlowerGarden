package com.flowergarden.flowers;

import com.flowergarden.properties.FreshnessInteger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Yevheniia Zubrych on 04.03.2018.
 */
public class ChamomileTest {

    private Chamomile chamomile;

    @Before
    public void init() {
        //Given
        chamomile = new Chamomile(2, 20, 5, new FreshnessInteger(5));
    }

    @Test
    public void testGettingPetal() throws Exception {
        Assert.assertEquals(true, chamomile.getPetal());
    }

    @Test
    public void testGettingTheLastPetal() throws Exception {
        //When
        chamomile.getPetal();
        chamomile.getPetal();
        boolean petalIsObtained = chamomile.getPetal();
        //Then
        Assert.assertEquals(false, petalIsObtained);
    }
}