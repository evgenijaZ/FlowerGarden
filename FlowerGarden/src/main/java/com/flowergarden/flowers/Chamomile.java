package com.flowergarden.flowers;

import com.flowergarden.properties.FreshnessInteger;

public class Chamomile extends GeneralFlower {

    private int petals;
    private int id;

    public Chamomile(int petals, int length, float price, FreshnessInteger fresh) {
        super(price,length,fresh);
        if (length < 0) throw new IllegalArgumentException("Chamomile length can`t be negative");
        if (price < 0) throw new IllegalArgumentException("Chamomile price can`t be negative");
        if (petals < 0) throw new IllegalArgumentException("Chamomile`s petals count can`t be negative");
        this.petals = petals;
        this.id = -1;

    }
        public Chamomile() {
        this.id = -1;
    }

    boolean getPetal() {
        if (petals <= 0) return false;
        petals -= 1;
        return true;
    }

    public int getPetals() {
        return petals;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
