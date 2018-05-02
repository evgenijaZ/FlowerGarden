package com.flowergarden.flowers;

import javax.xml.bind.annotation.XmlElement;

import com.flowergarden.properties.FreshnessInteger;

public class GeneralFlower implements Flower<Integer>, Comparable<GeneralFlower> {
	
	FreshnessInteger freshness;
	
	@XmlElement
	float price;
	
	@XmlElement
	int length;

    public GeneralFlower() {
    }

    public GeneralFlower(float price, int length, FreshnessInteger freshness) {
		this.price = price;
		this.length = length;
		this.freshness = freshness;
	}

	public void setFreshness(FreshnessInteger fr){
		freshness = fr;
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

}
