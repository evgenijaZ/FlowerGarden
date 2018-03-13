package com.flowergarden.bouquet;

public class MarriedBouquet extends GeneralBouquet {

    private float assemblePrice;

    public MarriedBouquet() {
        this.assemblePrice = 120;
    }

    public MarriedBouquet(float assemblePrice) {
        this.assemblePrice = assemblePrice;
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
}
