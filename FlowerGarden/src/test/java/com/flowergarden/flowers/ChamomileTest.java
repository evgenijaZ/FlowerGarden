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
        chamomile = new Chamomile(2, 20, 5, new FreshnessInteger(5));
    }

    @Test
    public void getPetalWhenItsCountIsMoreThanOneTest() throws Exception {
        Assert.assertEquals(true, chamomile.getPetal());
    }

    @Test
    public void getPetalWhenItsCountIsLessThanOneTest() throws Exception {
        chamomile.getPetal();
        chamomile.getPetal();
        boolean petalObtained = chamomile.getPetal();
        Assert.assertEquals(false, petalObtained);
    }
}