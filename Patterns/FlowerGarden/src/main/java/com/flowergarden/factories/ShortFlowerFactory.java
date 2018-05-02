package com.flowergarden.factories;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.Rose;
import com.flowergarden.properties.FreshnessInteger;

public class ShortFlowerFactory implements AbstractFlowerFactory {
    private static final int length = 5;

    private static final float rosePrice = 10;
    private static final float chamomilePrice = 8;
    private static final boolean spike = true;
    private static final int petals = 10;
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
