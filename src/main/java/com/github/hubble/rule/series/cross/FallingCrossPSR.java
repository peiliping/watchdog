package com.github.hubble.rule.series.cross;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.rule.series.PairSeriesRule;
import com.github.hubble.series.Series;


public class FallingCrossPSR extends PairSeriesRule<NumberET> {


    public FallingCrossPSR(String name, Series<NumberET> first, Series<NumberET> second) {

        super(name, first, second);
    }


    @Override public boolean match(long id) {

        NumberET e1 = super.first.get(id);
        NumberET e2 = super.second.get(id);
        if (NumCompareFunction.LT.apply(e1.getData(), e2.getData())) {
            NumberET e1b = super.first.getBefore(id);
            NumberET e2b = super.second.getBefore(id);
            return NumCompareFunction.GTE.apply(e1b.getData(), e2b.getData());
        }
        return false;
    }
}
