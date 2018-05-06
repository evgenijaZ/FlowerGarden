package com.flowergarden.bouquet;
import com.flowergarden.compisite.PriceInterface;

import java.util.Collection;

public interface Bouquet<T> extends PriceInterface{
	float getPrice();
	void addFlower(T flower);
	Collection<T> searchFlowersByLength(int start, int end);
	void sortByFreshness();
	Collection<T> getFlowers();
}
