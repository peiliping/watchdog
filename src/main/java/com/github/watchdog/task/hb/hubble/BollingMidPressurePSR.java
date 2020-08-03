package com.github.watchdog.task.hb.hubble;


import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.rule.series.pair.PairSeriesRule;
import com.github.hubble.series.Series;


public class BollingMidPressurePSR extends PairSeriesRule<TernaryNumberET> {


    public BollingMidPressurePSR(String name, Series<TernaryNumberET> polars, Series<TernaryNumberET> bolling) {

        super(name, polars, bolling);
        super.continuousStep = 5;
    }


    @Override protected boolean match(long id) {

        //当前k线的最高价要低于中轨
        TernaryNumberET polar1 = super.first.get(id);
        TernaryNumberET bolling1 = super.second.get(id);
        if (polar1.getFirst() > bolling1.getSecond()) {
            return false;
        }
        //倒数第二个k线的最高价需要低于中轨
        TernaryNumberET polar2 = super.first.getBefore(id, 1);
        TernaryNumberET bolling2 = super.second.getBefore(id, 1);
        if (polar2.getFirst() > bolling2.getSecond()) {
            return false;
        }
        //倒数第三个k线需要穿过中轨
        TernaryNumberET polar3 = super.first.getBefore(id, 2);
        TernaryNumberET bolling3 = super.second.getBefore(id, 2);
        if (!polar3.isInBox(bolling3.getSecond())) {
            return false;
        }

        int offset = 3;
        while (true) {
            TernaryNumberET polarX = super.first.getBefore(id, offset);
            TernaryNumberET bollingX = super.second.getBefore(id, offset);
            if (polarX.getFirst() < bollingX.getSecond()) {
                return true;
            }
            if (polarX.isInBox(bollingX.getSecond())) {
                offset++;
                continue;
            }
            if (polarX.getThird() > bollingX.getSecond()) {
                return false;
            }
        }
    }
}
