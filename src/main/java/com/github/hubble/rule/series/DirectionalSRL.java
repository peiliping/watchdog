package com.github.hubble.rule.series;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.series.Series;


public class DirectionalSRL extends SeriesRule<NumberET> {


    protected int step;

    protected int atLeast;

    protected NumCompareFunction numCompareFunction;


    public DirectionalSRL(String name, Series<NumberET> series, int step, int atLeast, NumCompareFunction numCompareFunction) {

        super(name, series);
        super.continuousStep = step;
        this.step = step;
        this.atLeast = atLeast;
        this.numCompareFunction = numCompareFunction;
    }


    @Override protected boolean match(long id) {

        int x = super.continuousStep - 1;
        NumberET first = super.series.getBefore(id, x);
        NumberET last = first;
        int m = 0;
        while (x-- > 0) {
            NumberET cur = super.series.getBefore(id, x);
            m += (this.numCompareFunction.apply(cur.getData(), last.getData()) ? 1 : 0);
            last = cur;
        }
        return (m >= this.atLeast) && this.numCompareFunction.apply(last.getData(), first.getData());
    }


    public static class DownDSRL extends DirectionalSRL {


        public DownDSRL(String name, Series<NumberET> series, int step) {

            super(name, series, step, step, NumCompareFunction.LT);
        }
    }


    public static class UpDSRL extends DirectionalSRL {


        public UpDSRL(String name, Series<NumberET> series, int step) {

            super(name, series, step, step, NumCompareFunction.GT);
        }
    }

}
