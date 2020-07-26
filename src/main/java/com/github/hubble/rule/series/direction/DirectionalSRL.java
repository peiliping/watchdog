package com.github.hubble.rule.series.direction;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.rule.series.SeriesRule;
import com.github.hubble.series.Series;


public class DirectionalSRL extends SeriesRule<NumberET> {


    protected double ratio;

    protected int step;

    protected NumCompareFunction numCompareFunction;


    public DirectionalSRL(String name, Series<NumberET> series, double ratio, int step, NumCompareFunction numCompareFunction) {

        super(name, series);
        this.ratio = ratio;
        this.step = step;
        this.numCompareFunction = numCompareFunction;
    }


    @Override protected boolean match(long id) {

        NumberET first = super.series.get(id - this.step * super.series.getInterval());
        NumberET last = first;
        if (first == null) {
            return false;
        }
        double c = 0, m = 0;
        for (long i = first.getId() + super.series.getInterval(); i <= id; i += super.series.getInterval()) {
            NumberET e = super.series.get(i);
            if (e != null) {
                c++;
                m = m + (this.numCompareFunction.apply(e.getData(), last.getData()) ? 1 : 0);
            }
            last = e;
        }
        return ((m / c) >= this.ratio) && this.numCompareFunction.apply(last.getData(), first.getData());
    }
}
