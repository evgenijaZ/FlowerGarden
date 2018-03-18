package com.flowergarden.flowers;

import com.flowergarden.properties.FreshnessInteger;

public class Chamomile extends GeneralFlower {

    private int petals;

    public Chamomile(int petals, int length, float price, FreshnessInteger fresh) {
        if (length < 0) throw new IllegalArgumentException("Chamomile length can`t be negative");
        if (price < 0) throw new IllegalArgumentException("Chamomile price can`t be negative");
        if (petals < 0) throw new IllegalArgumentException("Chamomile`s petals count can`t be negative");
        this.petals = petals;
        this.length = length;
        this.price = price;
        this.freshness = fresh;
    }
        public Chamomile() {
    }

    public boolean getPetal() {
        if (petals <= 0) return false;
        petals -= 1;
        return true;
    }

    public int getPetals() {
        return petals;
    }


}
