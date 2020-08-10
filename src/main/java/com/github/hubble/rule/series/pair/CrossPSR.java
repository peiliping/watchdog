package com.github.hubble.rule.series.pair;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.series.Series;


public abstract class CrossPSR extends PairSeriesRule<NumberET> {


    protected NumCompareFunction f1;

    protected NumCompareFunction f2;


    public CrossPSR(String name, Series<NumberET> first, Series<NumberET> second) {

        super(name, first, second);
        super.continuousStep = 5;
    }


    @Override public boolean match(long id) {

        NumberET e11 = super.first.getBefore(id, 0);
        NumberET e12 = super.second.getBefore(id, 0);
        NumberET e21 = super.first.getBefore(id, 1);
        NumberET e22 = super.second.getBefore(id, 1);
        if (this.f1.apply(e11.getData(), e12.getData()) && this.f1.apply(e21.getData(), e22.getData())) {
            NumberET e41 = super.first.getBefore(id, 3);
            NumberET e42 = super.second.getBefore(id, 3);
            NumberET e51 = super.first.getBefore(id, 4);
            NumberET e52 = super.second.getBefore(id, 4);
            return this.f2.apply(e41.getData(), e42.getData()) && this.f2.apply(e51.getData(), e52.getData());
        }
        return false;
    }


    public static class FallingCrossPSR extends CrossPSR {


        public FallingCrossPSR(String name, Series<NumberET> first, Series<NumberET> second) {

            super(name, first, second);
            super.f1 = NumCompareFunction.LT;
            super.f2 = NumCompareFunction.GT;
        }
    }


    public static class RisingCrossPSR extends CrossPSR {


        public RisingCrossPSR(String name, Series<NumberET> first, Series<NumberET> second) {

            super(name, first, second);
            super.f1 = NumCompareFunction.GT;
            super.f2 = NumCompareFunction.LT;
        }
    }
}
