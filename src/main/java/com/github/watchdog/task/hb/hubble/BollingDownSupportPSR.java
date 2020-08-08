package com.github.watchdog.task.hb.hubble;


import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.rule.series.pair.PairSeriesRule;
import com.github.hubble.series.Series;


public class BollingDownSupportPSR extends PairSeriesRule<TernaryNumberET> {


    public
    BollingDownSupportPSR(String name, Series<TernaryNumberET> polars, Series<TernaryNumberET> bolling) {

        super(name, polars, bolling);
        super.continuousStep = 5;
    }


    @Override protected boolean match(long id) {

        //当前k线的最低价要超过下轨
        TernaryNumberET polar1 = super.first.get(id);
        TernaryNumberET bolling1 = super.second.get(id);
        if (polar1.getThird() <= bolling1.getThird()) {
            return false;
        }
        //倒数第二个k线的最低价需要超过下轨
        TernaryNumberET polar2 = super.first.getBefore(id, 1);
        TernaryNumberET bolling2 = super.second.getBefore(id, 1);
        if (polar2.getThird() <= bolling2.getThird()) {
            return false;
        }

        //倒数第三个k线的最低价需要低于下轨
        TernaryNumberET polar3 = super.first.getBefore(id, 2);
        TernaryNumberET bolling3 = super.second.getBefore(id, 2);
        if (polar3.getThird() > bolling3.getThird()) {
            return false;
        }
        //倒数第四个k线的最低价需要低于下轨
        TernaryNumberET polar4 = super.first.getBefore(id, 3);
        TernaryNumberET bolling4 = super.second.getBefore(id, 3);
        if (polar4.getThird() > bolling4.getThird()) {
            return false;
        }
        return true;
    }
}
