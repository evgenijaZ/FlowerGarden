package com.flowergarden.flowers;

import javax.xml.bind.annotation.XmlElement;

import com.flowergarden.observer.Customer;
import com.flowergarden.observer.Observable;
import com.flowergarden.properties.FreshnessInteger;

import java.util.ArrayList;
import java.util.List;

public class GeneralFlower implements Flower<Integer>, Comparable<GeneralFlower>, Observable<Customer, FreshnessInteger> {

    FreshnessInteger freshness;

    @XmlElement
    float price;

    @XmlElement
    int length;

    private List<Customer> observers = new ArrayList<>();

    public GeneralFlower() {
    }

    public GeneralFlower(float price, int length, FreshnessInteger freshness) {
        this.price = price;
        this.length = length;
        this.freshness = freshness;
    }

    public void setFreshness(FreshnessInteger fr) {
        freshness = fr;
        notifyObservers(fr);
    }

    @Override
    public FreshnessInteger getFreshness() {
        return freshness;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int compareTo(GeneralFlower compareFlower) {
        int compareFresh = compareFlower.getFreshness().getFreshness();
        return this.getFreshness().getFreshness() - compareFresh;
    }

    @Override
    public void addObserver(Customer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Customer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(FreshnessInteger newFreshness) {
        for (Customer observer : observers) {
            observer.handleUpdating(newFreshness);
        }
    }
}
