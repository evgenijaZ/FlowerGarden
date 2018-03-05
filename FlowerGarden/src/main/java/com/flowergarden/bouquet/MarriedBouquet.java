package com.flowergarden.bouquet;

import com.flowergarden.flowers.GeneralFlower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MarriedBouquet implements Bouquet <GeneralFlower> {

    private float assemblePrice = 120;
    private List <GeneralFlower> flowerList = new ArrayList <>();

    @Override
    public float getPrice() {
        float price = assemblePrice;
        for (GeneralFlower flower : flowerList) {
            price += flower.getPrice();
        }
        return price;
    }

    @Override
    public void addFlower(GeneralFlower flower) {
        flowerList.add(flower);
    }

    @Override
    public Collection <GeneralFlower> searchFlowersByLength(int start, int end) {
        List <GeneralFlower> searchResult = new ArrayList <>();
        for (GeneralFlower flower : flowerList) {
            if (flower.getLength() >= start && flower.getLength() <= end) {
                searchResult.add(flower);
            }
        }
        return searchResult;
    }

    @Override
    public void sortByFreshness() {
        Collections.sort(flowerList);
    }

    @Override
    public Collection <GeneralFlower> getFlowers() {
        return flowerList;
    }

    public void setAssembledPrice(float price) {
        assemblePrice = price;
    }

    public float getAssemblePrice() {
        return assemblePrice;
    }

    void setMockedFlowerList(List <GeneralFlower> flowerList) {
        this.flowerList = flowerList;
    }
}
