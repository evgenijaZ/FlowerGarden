package com.flowergarden.creators;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;

public class ChamomileCreator extends FlowerCreator {
    @Override
    public GeneralFlower createFlower() {
        GeneralFlower flower = super.createFlower();
        return new Chamomile(15, flower.getLength(), flower.getPrice(), flower.getFreshness());
    }
}
