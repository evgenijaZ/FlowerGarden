package com.flowergarden.context;

import com.flowergarden.bouquet.MarriedBouquet;
import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;
import com.flowergarden.properties.FreshnessInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

/**
 * @author Yevheniia Zubrych on 25.03.2018.
 */
@Configuration
@PropertySource("file:src/main/resources/bouquet.properties")
public class ApplicationContextTest {

    @Value("${freshness1.freshness}")
    int freshness1Freshness;
    @Value("${freshness2.freshness}")
    int freshness2Freshness;
    @Value("${freshness3.freshness}")
    int freshness3Freshness;

    @Value("${flower1.price}")
    float flower1Price;
    @Value("${flower2.price}")
    float flower2Price;
    @Value("${flower3.price}")
    float flower3Price;
    @Value("${flower4.price}")
    float flower4Price;

    @Value("${flower1.length}")
    int flower1Length;
    @Value("${flower2.length}")
    int flower2Length;
    @Value("${flower3.length}")
    int flower3Length;
    @Value("${flower4.length}")
    int flower4length;

    @Value("${chamomile1.petals}")
    int chamomile1Petals;
    @Value("${chamomile2.petals}")
    int chamomile2Petals;

    @Value("${rose.spike}")
    boolean roseSpike;

    @Value("${bouquet1.price}")
    int bouquet1Price;
    @Value("${bouquet2.price}")
    int bouquet2Price;

    @Bean
    public FreshnessInteger freshness1() {
        return new FreshnessInteger(freshness1Freshness);
    }

    @Bean
    public FreshnessInteger freshness2() {
        return new FreshnessInteger(freshness2Freshness);
    }

    @Bean
    public FreshnessInteger freshness3() {
        return new FreshnessInteger(freshness3Freshness);
    }

    @Bean
    public Chamomile chamomile1() {
        return new Chamomile(chamomile1Petals, flower1Length, flower1Price, freshness1());
    }

    @Bean
    public Chamomile chamomile2() {
        return new Chamomile(chamomile2Petals, flower2Length, flower2Price, freshness2());
    }

    @Bean
    public Rose rose() {
        return new Rose(roseSpike, flower3Length, flower3Price, freshness1());
    }

    @Bean
    @Scope("prototype")
    public GeneralFlower flower1() {
        return new GeneralFlower(flower1Price, flower1Length, freshness1());
    }

    @Bean
    @Scope("prototype")
    public GeneralFlower flower2() {
        return new GeneralFlower(flower2Price, flower2Length, freshness2());
    }

    @Bean
    @Scope("prototype")
    public GeneralFlower flower3() {
        return new GeneralFlower(flower3Price, flower3Length, freshness3());
    }

    @Bean
    @Scope("prototype")
    public GeneralFlower flower4() {
        return new GeneralFlower(flower3Price, flower3Length, freshness2());
    }

    @Bean
    @Scope("prototype")
    public MarriedBouquet bouquet1() {
        return new MarriedBouquet(bouquet1Price);
    }

    @Bean
    @Scope("prototype")
    public MarriedBouquet bouquet2() {
        return new MarriedBouquet(bouquet2Price);
    }

    @Bean
    @Scope("prototype")
    public MarriedBouquet bouquet3() {
        return new MarriedBouquet();
    }
}
