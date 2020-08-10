package com.github.hubble.rule.series.threshold;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.rule.series.SeriesRule;
import com.github.hubble.series.Series;


public class InRangeSRL extends SeriesRule<NumberET> {


    private final double up;

    private final double down;

    private final NumCompareFunction upF;

    private final NumCompareFunction downF;


    public InRangeSRL(String name, Series<NumberET> series, double up, boolean includedUp, double down, boolean includedDown) {

        super(name, series);
        this.up = up;
        this.down = down;
        this.upF = includedUp ? NumCompareFunction.LTE : NumCompareFunction.LT;
        this.downF = includedDown ? NumCompareFunction.GTE : NumCompareFunction.GT;
    }


    public InRangeSRL(String name, Series<NumberET> series, double up, double down) {

        this(name, series, up, false, down, false);
    }


    @Override protected boolean match(long id) {

        NumberET num = super.series.get(id);
        return this.upF.apply(num.getData(), this.up) && this.downF.apply(num.getData(), this.down);
    }
}
