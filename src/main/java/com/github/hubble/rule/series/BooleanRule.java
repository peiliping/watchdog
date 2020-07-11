package com.github.hubble.rule.series;


import com.github.hubble.Series;
import com.github.hubble.ele.BooleanET;


public class BooleanRule extends SeriesRule<BooleanET> {


    public BooleanRule(String name, Series<BooleanET> series) {

        super(name, series);
    }


    @Override public boolean isMatched(long id) {

        BooleanET singleBooleanET = super.series.get(id);
        return singleBooleanET != null && singleBooleanET.isData();
    }
}
