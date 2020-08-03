package com.github.watchdog.task.hb.hubble;


import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.rule.series.pair.PairSeriesRule;
import com.github.hubble.series.Series;


public class BollingMidRisingPSR extends PairSeriesRule<TernaryNumberET> {


    public BollingMidRisingPSR(String name, Series<TernaryNumberET> polars, Series<TernaryNumberET> bolling) {

        super(name, polars, bolling);
        super.continuousStep = 3;
    }


    @Override protected boolean match(long id) {

        //当前k线的最高价要超过中轨
        TernaryNumberET polar1 = super.first.get(id);
        TernaryNumberET bolling1 = super.second.get(id);
        if (polar1.getFirst() <= bolling1.getSecond()) {
            return false;
        }
        //倒数第二个k线的最高价需要在中轨之下
        TernaryNumberET polar2 = super.first.getBefore(id, 1);
        TernaryNumberET bolling2 = super.second.getBefore(id, 1);
        if (polar2.getFirst() >= bolling2.getSecond()) {
            return false;
        }
        //倒数第三个k线的最高价需要在中轨之下
        TernaryNumberET polar3 = super.first.getBefore(id, 2);
        TernaryNumberET bolling3 = super.second.getBefore(id, 2);
        if (polar3.getFirst() >= bolling3.getSecond()) {
            return false;
        }
        return true;
    }
}
