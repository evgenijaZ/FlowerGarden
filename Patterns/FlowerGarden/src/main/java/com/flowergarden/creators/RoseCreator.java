package com.flowergarden.creators;

import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;

public class RoseCreator extends FlowerCreator {
    @Override
    public GeneralFlower createFlower() {
        GeneralFlower flower = super.createFlower();
        return new Rose(false, flower.getLength(), flower.getPrice(), flower.getFreshness());
    }
}
