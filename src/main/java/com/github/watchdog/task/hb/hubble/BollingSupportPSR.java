package com.github.watchdog.task.hb.hubble;


import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.rule.series.pair.PairSeriesRule;
import com.github.hubble.series.Series;


public class BollingSupportPSR extends PairSeriesRule<TernaryNumberET> {


    public BollingSupportPSR(String name, Series<TernaryNumberET> polars, Series<TernaryNumberET> bolling) {

        super(name, polars, bolling);
        super.continuousStep = 3;
    }


    @Override protected boolean match(long id) {

        TernaryNumberET polar1 = super.first.get(id);
        TernaryNumberET bolling1 = super.second.get(id);
        if (polar1.getThird() <= bolling1.getThird()) {
            return false;
        }

        TernaryNumberET polar2 = super.first.getBefore(id, 1);
        TernaryNumberET bolling2 = super.second.getBefore(id, 1);
        if (polar2.getThird() <= bolling2.getThird()) {
            return false;
        }
        TernaryNumberET polar3 = super.first.getBefore(id, 2);
        TernaryNumberET bolling3 = super.second.getBefore(id, 2);
        if (polar3.getThird() > bolling3.getThird()) {
            return false;
        }
        return true;
    }
}
