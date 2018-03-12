package com.flowergarden.bouquet;

public class MarriedBouquet extends GeneralBouquet {

    private float assemblePrice = 120;

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
