package com.github.hubble.rule.series;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;


public class ShockSeriesRule extends SeriesRule<CandleET> {


    private double ratio;

    private int count;


    public ShockSeriesRule(String name, Series<CandleET> series, double ratio, int count) {

        super(name, series);
        this.ratio = ratio;
        this.count = count;
    }


    @Override public boolean isMatched() {

        if (super.series.getMaxId() == 0) {
            return false;
        }
        return true;

        //        List<CandleET> lastN = Lists.newArrayList();
        //        for (long i = super.series.getMaxId() - this.count * super.series.getInterval();
        //             i < super.series.getMaxId(); i += super.series.getInterval()) {
        //
        //        }

    }
}
