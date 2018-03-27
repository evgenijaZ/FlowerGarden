package com.flowergarden.flowers;

import com.flowergarden.properties.FreshnessInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GeneralFlower implements Flower <Integer>, Comparable <GeneralFlower> {
    @XmlElement
    private
    FreshnessInteger freshness;
    @XmlElement
    private
    float price;
    @XmlElement
    private
    int length;
    @XmlElement
    private int id;


    public GeneralFlower(float price, int length, FreshnessInteger freshness) {
        this.freshness = freshness;
        this.price = price;
        this.length = length;
        this.id = -1;
    }

    public GeneralFlower() {
        this.id = -1;
    }

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

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int compareTo(GeneralFlower compareFlower) {
        int compareFresh = compareFlower.getFreshness().getFreshness();
        return this.getFreshness().getFreshness() - compareFresh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof GeneralFlower))
            return false;
        GeneralFlower flower = (GeneralFlower) obj;
        return (this.price == flower.price)
                && (this.length - flower.length < 0.001)
                && Objects.equals(this.getFreshness(), flower.getFreshness());
    }
}
