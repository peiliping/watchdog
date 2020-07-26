package com.github.hubble.rule.series;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.series.Series;


public class NumberComparePSR extends PairSeriesRule<NumberET> {


    private NumCompareFunction numCompareFunction;


    public NumberComparePSR(String name, Series<NumberET> first, Series<NumberET> second, NumCompareFunction numCompareFunction) {

        super(name, first, second);
        this.numCompareFunction = numCompareFunction;
    }


    @Override public boolean match(long id) {

        NumberET e1 = super.first.get(id);
        NumberET e2 = super.second.get(id);
        return this.numCompareFunction.apply(e1.getData(), e2.getData());
    }
}
