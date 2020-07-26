package com.github.hubble.rule.series.threshold;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.rule.series.SeriesRule;
import com.github.hubble.series.Series;


public class ThresholdSRL extends SeriesRule<NumberET> {


    private double threshold;

    private NumCompareFunction numCompareFunction;


    public ThresholdSRL(String name, Series<NumberET> series, double threshold, NumCompareFunction ncFunction) {

        super(name, series);
        this.threshold = threshold;
        this.numCompareFunction = ncFunction;
    }


    @Override protected boolean match(long id) {

        NumberET num = super.series.get(id);
        return this.numCompareFunction.apply(num.getData(), this.threshold);
    }
}
