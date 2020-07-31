
package com.github.hubble.trend;


import com.github.hubble.trend.constants.Period;
import com.github.hubble.trend.constants.TrendDegree;
import com.github.hubble.trend.constants.TrendType;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;


@Getter
@ToString
public class TrendEntity {


    private final Period period;

    @ToString.Exclude
    private final TrendRule trendRule;

    @ToString.Exclude
    private final Map<TrendType, TrendRule> degreeRules;

    private TrendType trendType = TrendType.SHOCK;

    private TrendDegree trendDegree = TrendDegree.UNCERTAIN;


    public TrendEntity(Period period, TrendRule trendRule, Map<TrendType, TrendRule> degreeRules) {

        this.period = period;
        this.trendRule = trendRule;
        this.degreeRules = degreeRules;
    }


    public void update() {

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
    }
}
