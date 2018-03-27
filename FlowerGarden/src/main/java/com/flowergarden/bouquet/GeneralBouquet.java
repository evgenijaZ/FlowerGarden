package com.flowergarden.bouquet;

import com.flowergarden.dao.json.FlowerAdapter;
import com.flowergarden.flowers.GeneralFlower;
import com.sun.xml.internal.txw2.annotation.XmlElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Yevheniia Zubrych on 12.03.2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class GeneralBouquet implements Bouquet <GeneralFlower> {
    @XmlJavaTypeAdapter(FlowerAdapter.class)
    private List <GeneralFlower> flowerList = new ArrayList <>();
    private int id;
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
