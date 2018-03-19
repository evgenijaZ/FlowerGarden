package com.flowergarden.flowers;

import com.flowergarden.properties.FreshnessInteger;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Rose extends GeneralFlower {

    private int id;
    private boolean spike;

    public Rose(boolean spike, int length, float price, FreshnessInteger fresh) {
        if (length < 0) throw new IllegalArgumentException("Rose length can`t be negative");
        if (price < 0) throw new IllegalArgumentException("Rose price can`t be negative");
        this.spike = spike;
        this.length = length;
        this.price = price;
        this.freshness = fresh;
    }

    public Rose() {
    }

    public boolean getSpike() {
        return spike;
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
