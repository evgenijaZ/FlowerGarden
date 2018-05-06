package com.flowergarden.bouquet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.iterator.Container;
import com.flowergarden.iterator.Iterator;

public class MarriedBouquet implements Bouquet<GeneralFlower>, Cloneable, Container {

	private float assemblePrice = 120;
	private List<GeneralFlower> flowerList = new ArrayList<>();

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
	public Collection<GeneralFlower> searchFlowersByLength(int start, int end) {
		List<GeneralFlower> searchResult = new ArrayList<GeneralFlower>();
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
	public Collection<GeneralFlower> getFlowers() {
		return flowerList;
	}

	public void setAssembledPrice(float price) {
		assemblePrice = price;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		MarriedBouquet clone  = (MarriedBouquet) super.clone();
		clone.flowerList = new ArrayList<>(this.flowerList);
		return clone;
	}

	@Override
	public Iterator getIterator() {
		return new FlowerIterator();
	}

	private class FlowerIterator implements Iterator {
		int index;
		@Override
		public boolean hasNext() {
			return index < flowerList.size();
		}

		@Override
		public Object next() {
			if(this.hasNext()){
				return flowerList.get(index++);
			}
			return null;
		}
	}
}
