package com.flowergarden.decorators;

import com.flowergarden.flowers.Flower;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.properties.Freshness;

public class FlowerDecorator implements Flower<Integer> {
    private GeneralFlower flower;

    public FlowerDecorator(GeneralFlower flower) {
        this.flower = flower;
    }

    @Override
    public Freshness<Integer> getFreshness() {
       return flower.getFreshness();
    }

    @Override
    public float getPrice() {
       return flower.getPrice();
    }

    @Override
    public int getLength() {
       return flower.getLength();
    }
}
