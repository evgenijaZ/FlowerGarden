package com.flowergarden.adapters;

import com.flowergarden.flowers.GeneralFlower;

import java.util.Collection;

public interface SearchFlower {
    Collection<GeneralFlower> searchFlowerLongerThen(int start);
}
