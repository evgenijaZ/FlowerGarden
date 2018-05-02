package com.flowergarden.service;

import org.springframework.stereotype.Service;

import com.flowergarden.bouquet.Bouquet;

@Service
public class BouquetService {
	
	public float getBouquetPrice(Bouquet bouquet){
		return bouquet.getPrice();
	}

}
