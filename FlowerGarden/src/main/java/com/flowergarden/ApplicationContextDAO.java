package com.flowergarden;

import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.dao.BouquetDAO;
import com.flowergarden.dao.FlowerDAO;
import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;
import com.flowergarden.properties.FreshnessInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

/**
 * @author Yevheniia Zubrych on 25.03.2018.
 */
@Configuration
@Import(ApplicationContextTest.class)
@PropertySource("classpath:db.properties")
public class ApplicationContextDAO {

    @Value("${db.url}") String dbUrl;
    @Value("${db.name}") String dbName;
    @Value("${db.schema}") String dbSchema;

    @Value("${db.tables.bouquet}") String bouquetTable;
    @Value("${db.tables.chamomile}") String chamomileTable;
    @Value("${db.tables.rose}") String roseTable;
    @Value("${db.tables.flower}") String flowerTable;

    @Bean
    public BouquetDAO bouquetDAO(){
        return new BouquetDAO(dataSource(), dbSchema, bouquetTable);
    }

    @Bean
    public FlowerDAO flowerDAO(){
        return new FlowerDAO(dataSource(), dbSchema, flowerTable);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl(url());
        return dataSource;
    }

    @Bean
    public String url() {
        File dbFile = new File(dbName);
        try {
            return  dbUrl+dbFile.getCanonicalFile().toURI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
