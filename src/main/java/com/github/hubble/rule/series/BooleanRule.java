package com.github.hubble.rule.series;


import com.github.hubble.Series;
import com.github.hubble.ele.SingleBooleanET;


public class BooleanRule extends SeriesRule<SingleBooleanET> {


    public BooleanRule(String name, Series<SingleBooleanET> series) {

        super(name, series);
    }


    @Override public boolean isMatched() {

        SingleBooleanET singleBooleanET = super.series.getLast();
        return singleBooleanET != null && singleBooleanET.isData();
    }
}
