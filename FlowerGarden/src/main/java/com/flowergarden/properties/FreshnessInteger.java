package com.flowergarden.properties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
public class FreshnessInteger implements Freshness <Integer>, Comparable <FreshnessInteger> {

    @XmlElement
    private Integer freshness;

    public FreshnessInteger(Integer freshness) {
        this.freshness = freshness;
    }

    public FreshnessInteger() {

    }

    @Override
    public Integer getFreshness() {
        return freshness;
    }

    @Override
    public int compareTo(FreshnessInteger o) {
        if (freshness > o.getFreshness()) return 1;
        if (freshness < o.getFreshness()) return -1;
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof FreshnessInteger))
            return false;
        FreshnessInteger freshness = (FreshnessInteger) obj;
        return Objects.equals(this.freshness, freshness.freshness);
    }
}
