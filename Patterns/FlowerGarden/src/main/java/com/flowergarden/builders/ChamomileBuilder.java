package com.flowergarden.builders;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;

public class ChamomileBuilder extends FlowerBuilder {
    private int petals;

    public ChamomileBuilder setPetals(int petals) {
        this.petals = petals;
        return this;
    }

    public Chamomile build() {
        GeneralFlower flower = super.build();
        return new Chamomile(petals, flower.getLength(), flower.getPrice(), flower.getFreshness());
    }
}
