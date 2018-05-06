package com.flowergarden.adapters;

import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.flowers.GeneralFlower;

import java.util.Collection;

public class BouquetAdapter extends MarriedBouquet implements SearchFlower {
    @Override
    public Collection<GeneralFlower> searchFlowerLongerThen(int start) {
        return super.searchFlowersByLength(start, Integer.MAX_VALUE);
    }
}
