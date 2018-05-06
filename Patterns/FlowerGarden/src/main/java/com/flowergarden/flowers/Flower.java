package com.flowergarden.flowers;

import com.flowergarden.compisite.PriceInterface;
import com.flowergarden.properties.Freshness;

public interface Flower<T> extends PriceInterface {
	Freshness<T> getFreshness();
	float getPrice();
	int getLength();
}
