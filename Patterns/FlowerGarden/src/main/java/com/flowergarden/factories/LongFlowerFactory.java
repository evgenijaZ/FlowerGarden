package com.flowergarden.factories;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.Rose;
import com.flowergarden.properties.FreshnessInteger;

public class LongFlowerFactory implements AbstractFlowerFactory {
    private static final int length = 15;

    private static final float rosePrice = 20;
    private static final float chamomilePrice = 16;
    private static final boolean spike = false;
    private static final int petals = 12;
    private static final int freshness = 10;

    @Override
    public Rose createRose() {
        return new Rose(spike, length, rosePrice, new FreshnessInteger(freshness));
    }

    @Override
    public Chamomile createChamomile() {
        return new Chamomile(petals, length, chamomilePrice, new FreshnessInteger(freshness));
    }
}
