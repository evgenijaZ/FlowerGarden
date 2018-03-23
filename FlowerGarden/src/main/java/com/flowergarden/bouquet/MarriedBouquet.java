package com.flowergarden.bouquet;

import com.flowergarden.flowers.GeneralFlower;

import java.util.Objects;

public class MarriedBouquet extends GeneralBouquet {

    private int id;
    private float assemblePrice;

    public MarriedBouquet() {
        this.assemblePrice = 120;
        this.id = -1;
    }

    public MarriedBouquet(float assemblePrice) {
        this.assemblePrice = assemblePrice;
        this.id = -1;
    }

    public MarriedBouquet(int id, float assemblePrice){
        this.id = id;
        this.assemblePrice = assemblePrice;
    }

    @Override
    public float getPrice() {
        return super.getPrice() + assemblePrice;
    }

    public void setAssembledPrice(float price) {
        assemblePrice = price;
    }

    public float getAssemblePrice() {
        return assemblePrice;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MarriedBouquet))
            return false;
        MarriedBouquet bouquet = (MarriedBouquet) obj;
        return (this.assemblePrice == bouquet.assemblePrice)
                && Objects.equals(this.getFlowers(), bouquet.getFlowers());
    }
}
