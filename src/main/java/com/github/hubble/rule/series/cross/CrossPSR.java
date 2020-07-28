package com.github.hubble.rule.series.cross;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.rule.series.PairSeriesRule;
import com.github.hubble.series.Series;


public abstract class CrossPSR extends PairSeriesRule<NumberET> {


    protected NumCompareFunction current;

    protected NumCompareFunction before1;

    protected NumCompareFunction before2;


    public CrossPSR(String name, Series<NumberET> first, Series<NumberET> second) {

        super(name, first, second);
    }


    @Override public boolean prepare(long id) {

        if (super.prepare(id)) {
            NumberET e11 = super.first.getBefore(id, 1);
            NumberET e12 = super.second.getBefore(id, 1);
            NumberET e21 = super.first.getBefore(id, 2);
            NumberET e22 = super.second.getBefore(id, 2);
            return e11 != null && e12 != null && e21 != null && e22 != null;
        }
        return false;
    }


    @Override public boolean match(long id) {

        NumberET e11 = super.first.get(id);
        NumberET e12 = super.second.get(id);
        if (this.current.apply(e11.getData(), e12.getData())) {
            NumberET e21 = super.first.getBefore(id, 1);
            NumberET e22 = super.second.getBefore(id, 1);
            NumberET e31 = super.first.getBefore(id, 2);
            NumberET e32 = super.second.getBefore(id, 2);
            return this.before1.apply(e21.getData(), e22.getData()) && this.before2.apply(e31.getData(), e32.getData());
        }
        return false;
    }
}
