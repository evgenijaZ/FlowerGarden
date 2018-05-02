package com.flowergarden.builders;

import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.properties.FreshnessInteger;

public class FlowerBuilder {
    private int length;
    private float price;
    private FreshnessInteger freshness;

    public FlowerBuilder setLength(int length) {
        this.length = length;
        return this;
    }

    public FlowerBuilder setPrice(float price) {
        this.price = price;
        return this;
    }

    public FlowerBuilder setFreshness(FreshnessInteger freshness) {
        this.freshness = freshness;
        return this;
    }

    public GeneralFlower build(){
        return new GeneralFlower(price, length, freshness);
    }
}
