
package com.github.hubble.trend;


import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.specific.BollingPIS;
import com.github.hubble.trend.constants.Period;
import com.github.hubble.trend.constants.TrendDegree;
import com.github.hubble.trend.constants.TrendType;
import com.github.watchdog.common.Util;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;


@Getter
public class TrendEntity {


    private final Period period;

    @ToString.Exclude
    private final TrendRule trendRule;

    @ToString.Exclude
    private final Map<TrendType, TrendRule> degreeRules;

    private final BollingPIS bolling;

    private TrendType trendType = TrendType.SHOCK;

    private TrendDegree trendDegree = TrendDegree.UNCERTAIN;


    public TrendEntity(Period period, TrendRule trendRule, Map<TrendType, TrendRule> degreeRules, BollingPIS bolling) {

        this.period = period;
        this.trendRule = trendRule;
        this.degreeRules = degreeRules;
        this.bolling = bolling;
    }


    public boolean update() {

        TrendType old = this.trendType;
        TrendDegree oldD = this.trendDegree;

        if (this.trendRule.getTrendTypeResult(TrendType.UPWARD)) {
            this.trendType = TrendType.UPWARD;
        } else if (this.trendRule.getTrendTypeResult(TrendType.DOWNWARD)) {
            this.trendType = TrendType.DOWNWARD;
        } else {
            this.trendType = TrendType.SHOCK;
        }

        TrendRule degreeRule = this.degreeRules.get(this.trendType);
        if (degreeRule.getTrendDegreeResult(TrendDegree.POSITIVE)) {
            this.trendDegree = TrendDegree.POSITIVE;
        } else if (degreeRule.getTrendDegreeResult(TrendDegree.NEGATIVE)) {
            this.trendDegree = TrendDegree.NEGATIVE;
        } else {
            this.trendDegree = TrendDegree.UNCERTAIN;
        }

        return old != this.trendType || oldD != this.trendDegree;
    }


    public double shockValue() {

        TernaryNumberET ele = this.bolling.getLast();
        return ele.getFirst() - ele.getThird();
    }


    public double shockRatio() {

        TernaryNumberET ele = this.bolling.getLast();
        return Util.formatPercent(ele.getFirst(), ele.getThird(), ele.getSecond());
    }


    public double upper() {

        TernaryNumberET ele = this.bolling.getLast();
        return ele.getFirst();
    }


    public double lower() {

        TernaryNumberET ele = this.bolling.getLast();
        return ele.getThird();
    }


    @Override public String toString() {

        return String.format("TE{P=%s,TT=%s,TD=%s,SR=%s%%}", this.period.name, this.trendType.name, this.trendDegree.name, shockRatio());
    }
}
