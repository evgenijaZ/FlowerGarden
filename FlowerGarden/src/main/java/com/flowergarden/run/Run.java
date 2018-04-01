package com.flowergarden.run;

import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.context.ApplicationContextDAO;
import com.flowergarden.dao.BouquetDAO;
import com.flowergarden.dao.json.JsonDAO;
import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.Rose;
import org.codehaus.jettison.json.JSONException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;


public class Run {

    public static void main(String[] args) throws JSONException, XMLStreamException {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextDAO.class);


        BouquetDAO dao = (BouquetDAO) context.getBean("bouquetDAO");
        MarriedBouquet bouquetFromDB = dao.getByKey(4);
        if (bouquetFromDB == null) {
            bouquetFromDB = (MarriedBouquet) context.getBean("bouquet1");

            bouquetFromDB.setId(4);

            Chamomile chamomile1 = (Chamomile) context.getBean("chamomile1");
            Chamomile chamomile2 = (Chamomile) context.getBean("chamomile2");

            Rose rose = (Rose) context.getBean("rose");

            bouquetFromDB.addFlower(chamomile1);
            bouquetFromDB.addFlower(chamomile2);
            bouquetFromDB.addFlower(rose);

            dao.create(bouquetFromDB);
        }

        System.out.println("Price of bouquet from data base: " + bouquetFromDB.getPrice());

        JsonDAO jsonDAO = (JsonDAO) context.getBean("jsonDAO");
        jsonDAO.create(bouquetFromDB);

        MarriedBouquet bouquetFromJSON = jsonDAO.getBouquet();

        System.out.println("Price of bouquet from json file: " + bouquetFromJSON.getPrice());
    }
}
