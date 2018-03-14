package com.flowergarden.run;

import com.flowergarden.bouquet.GeneralBouquet;
import com.flowergarden.dao.BouquetDAO;
import com.flowergarden.dao.FlowerDAO;
import com.flowergarden.flowers.GeneralFlower;

import java.util.List;

public class Run {

    public static void main(String[] args) {

        BouquetDAO bouquetDAO = new BouquetDAO();
        FlowerDAO flowerDAO = new FlowerDAO();
        GeneralBouquet bouquet = bouquetDAO.getByKey(1);
        List <GeneralFlower> flowers = flowerDAO.getByBouquetId(1);
        for (GeneralFlower flower : flowers) {
            bouquet.addFlower(flower);
        }
        System.out.println(bouquet.getPrice());
    }
}
