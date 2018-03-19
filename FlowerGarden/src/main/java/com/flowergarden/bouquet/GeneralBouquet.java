package com.flowergarden.bouquet;

import com.flowergarden.flowers.GeneralFlower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 12.03.2018.
 */
public abstract class GeneralBouquet implements Bouquet <GeneralFlower> {
    private List <GeneralFlower> flowerList = new ArrayList <>();
    int id;
    @Override
    public float getPrice() {
        float price = 0;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
