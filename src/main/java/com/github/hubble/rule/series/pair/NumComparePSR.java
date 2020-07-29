package com.github.hubble.rule.series.pair;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.series.Series;


public class NumComparePSR extends PairSeriesRule<NumberET> {


    private NumCompareFunction numCompareFunction;


    public NumComparePSR(String name, Series<NumberET> first, Series<NumberET> second, NumCompareFunction numCompareFunction) {

        super(name, first, second);
        this.numCompareFunction = numCompareFunction;
    }


    public NumComparePSR(String name, Series<NumberET> first, Series<NumberET> second, NumCompareFunction numCompareFunction, int step) {

        super(name, first, second);
        this.numCompareFunction = numCompareFunction;
        super.continuousStep = step;
    }


    @Override public boolean match(long id) {

        if (super.continuousStep == 1) {
            NumberET e1 = this.first.get(id);
            NumberET e2 = this.second.get(id);
            return this.numCompareFunction.apply(e1.getData(), e2.getData());
        }

        int x = super.continuousStep;
        while (x-- > 0) {
            NumberET e1 = this.first.getBefore(id, x);
            NumberET e2 = this.second.getBefore(id, x);
            if (!this.numCompareFunction.apply(e1.getData(), e2.getData())) {
                return false;
            }
        }
        return true;
    }
}
