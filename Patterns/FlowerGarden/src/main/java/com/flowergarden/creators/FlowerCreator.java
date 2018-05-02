package com.flowergarden.creators;

import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.properties.FreshnessInteger;

public class FlowerCreator {
    public GeneralFlower createFlower(){
        return new GeneralFlower(20,10, new FreshnessInteger(10));
    };
}
