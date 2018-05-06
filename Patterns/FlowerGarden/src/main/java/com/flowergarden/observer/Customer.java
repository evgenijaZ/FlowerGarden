package com.flowergarden.observer;

import com.flowergarden.properties.FreshnessInteger;

public class Customer implements Observer <FreshnessInteger>{
    @Override
    public void handleUpdating(FreshnessInteger freshness) {
        System.out.println("Updated value is received: "+ freshness);
    }
}
