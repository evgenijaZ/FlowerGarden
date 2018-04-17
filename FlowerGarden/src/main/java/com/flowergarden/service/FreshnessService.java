package com.flowergarden.service;

import com.flowergarden.bouquet.GeneralBouquet;
import com.flowergarden.exceptions.StaleFlowerException;
import com.flowergarden.flowers.GeneralFlower;

import java.util.Collection;

/**
 * @author Yevheniia Zubrych on 16.04.2018.
 */
public class FreshnessService {
    public static void decreaseFreshness(GeneralBouquet bouquet) throws StaleFlowerException {
        Collection <GeneralFlower> flowers = bouquet.getFlowers();
        for (GeneralFlower flower : flowers) {
           decreaseFreshness(flower);
        }
    }

    public static void decreaseFreshness(GeneralFlower flower) throws StaleFlowerException {
        flower.getFreshness().setFreshness(flower.getFreshness().getFreshness()-1);
        if (flower.getFreshness().getFreshness() == 0)
            throw new StaleFlowerException("Flower with id" + flower.getId() + " should be removed");
    }
}
