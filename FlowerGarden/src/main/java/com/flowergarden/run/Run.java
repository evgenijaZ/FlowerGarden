package com.flowergarden.run;

import com.flowergarden.ApplicationContextDAO;
import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.dao.BouquetDAO;
import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.Rose;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.sql.SQLException;


public class Run {

    public static void main(String[] args) throws SQLException {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextDAO.class);


        BouquetDAO dao = (BouquetDAO) context.getBean("bouquetDAO");
        MarriedBouquet bouquet1 = (MarriedBouquet) context.getBean("bouquet1");

        bouquet1.setId(4);

        Chamomile chamomile1 = (Chamomile) context.getBean("chamomile1");
        Chamomile chamomile2 = (Chamomile) context.getBean("chamomile2");

        Rose rose = (Rose) context.getBean("rose");

        bouquet1.addFlower(chamomile1);
        bouquet1.addFlower(chamomile2);
        bouquet1.addFlower(rose);

        dao.create(bouquet1);

        MarriedBouquet actualBouquet = dao.getByKey(4);

        System.out.println(actualBouquet.getPrice());
    }
}
