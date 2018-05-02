package com.flowergarden.builders;

import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;

public class RoseBuilder extends FlowerBuilder {
    private boolean spike;

    public RoseBuilder setSpike(boolean spike) {
        this.spike = spike;
        return this;
    }

    public Rose build(){
        GeneralFlower flower = super.build();
        return new Rose(spike, flower.getLength(), flower.getPrice(), flower.getFreshness());
    }
}
