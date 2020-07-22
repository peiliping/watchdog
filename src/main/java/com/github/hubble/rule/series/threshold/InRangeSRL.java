package com.github.hubble.rule.series.threshold;


import com.github.hubble.Series;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.rule.RuleResult;
import com.github.hubble.rule.series.SeriesRule;

import java.util.List;


public class InRangeSRL extends SeriesRule<NumberET> {


    private double up;

    private double down;


    public InRangeSRL(String name, Series<NumberET> series, double up, double down) {

        super(name, series);
        this.up = up;
        this.down = down;
    }


    @Override protected boolean match(long id, List<RuleResult> results) {

        NumberET num = super.series.get(id);
        return NumCompareFunction.LT.apply(num.getData(), this.up) && NumCompareFunction.GT.apply(num.getData(), this.down);
    }
}
