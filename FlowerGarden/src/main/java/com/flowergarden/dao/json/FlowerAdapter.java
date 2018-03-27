package com.flowergarden.dao.json;

import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;
import com.flowergarden.properties.FreshnessInteger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Yevheniia Zubrych on 27.03.2018.
 */
public class FlowerAdapter extends XmlAdapter <FlowerAdapter.AdaptedFlower, GeneralFlower> {


    @Override
    public GeneralFlower unmarshal(AdaptedFlower v) {
        if (v == null) return null;
        if (null != v.petals) {
            return new Chamomile(v.petals, v.getLength(), v.getPrice(), v.getFreshness());
        }
        if (null != v.spike) {
            return new Rose(v.spike, v.getLength(), v.getPrice(), v.getFreshness());
        }
        return new GeneralFlower(v.getPrice(), v.getLength(), v.getFreshness());
    }


    @Override
    public AdaptedFlower marshal(GeneralFlower v) {
        if (v == null) return null;
        AdaptedFlower adaptedFlower = new AdaptedFlower(v.getPrice(), v.getLength(), v.getFreshness());
        if (v instanceof Chamomile) {
            adaptedFlower.petals = ((Chamomile) v).getPetals();
            return adaptedFlower;
        }
        if (v instanceof Rose) {
            adaptedFlower.spike = ((Rose) v).getSpike();
            return adaptedFlower;
        }
        return adaptedFlower;
    }

    public static class AdaptedFlower extends GeneralFlower {
        @XmlAttribute
        Boolean spike;
        @XmlAttribute
        Integer petals;

        AdaptedFlower(){}

        AdaptedFlower(float price, int length, FreshnessInteger freshness) {
            super(price, length, freshness);
        }
    }
}
