package com.github.hubble.rule.series;


import com.github.hubble.Series;
import com.github.hubble.ele.BooleanET;
import com.github.hubble.RuleResult;

import java.util.List;


public class BooleanRule extends SeriesRule<BooleanET> {


    public BooleanRule(String name, Series<BooleanET> series) {

        super(name, series);
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        BooleanET singleBooleanET = super.series.get(id);
        return singleBooleanET != null && singleBooleanET.isData();
    }
}
