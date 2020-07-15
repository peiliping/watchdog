package com.github.hubble.rule.series;


import com.github.hubble.Series;
import com.github.hubble.ele.BooleanET;
import com.github.hubble.rule.RuleResult;

import java.util.List;


public class BooleanSeriesRule extends SeriesRule<BooleanET> {


    public BooleanSeriesRule(String name, Series<BooleanET> series) {

        super(name, series);
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        return super.series.get(id).isData();
    }
}
