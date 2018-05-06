package com.flowergarden.decorators;

import com.flowergarden.flowers.Flower;
import com.flowergarden.flowers.GeneralFlower;

public class ColoredFlower extends FlowerDecorator{
    private int color;

    public ColoredFlower(GeneralFlower flower) {
        super(flower);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
