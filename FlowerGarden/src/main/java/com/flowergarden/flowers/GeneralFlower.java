package com.flowergarden.flowers;

import com.flowergarden.properties.FreshnessInteger;

import javax.xml.bind.annotation.XmlElement;

public class GeneralFlower implements Flower <Integer>, Comparable <GeneralFlower> {

    FreshnessInteger freshness;

    @XmlElement
    float price;

    @XmlElement
    int length;

    @Override
    public FreshnessInteger getFreshness() {
        return freshness;
    }

    public void setFreshness(FreshnessInteger fr) {
        freshness = fr;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int compareTo(GeneralFlower compareFlower) {
        int compareFresh = compareFlower.getFreshness().getFreshness();
        return this.getFreshness().getFreshness() - compareFresh;
    }

}
