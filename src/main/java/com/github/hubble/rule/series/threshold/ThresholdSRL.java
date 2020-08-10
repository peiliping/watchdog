package com.github.hubble.rule.series.threshold;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.rule.series.SeriesRule;
import com.github.hubble.series.Series;


public class ThresholdSRL extends SeriesRule<NumberET> {


    private final double threshold;

    private final NumCompareFunction numCompareFunction;


    public ThresholdSRL(String name, Series<NumberET> series, double threshold, int step, NumCompareFunction ncFunction) {

        super(name, series);
        super.continuousStep = step;
        this.threshold = threshold;
        this.numCompareFunction = ncFunction;
    }


    public ThresholdSRL(String name, Series<NumberET> series, double threshold, NumCompareFunction ncFunction) {

        this(name, series, threshold, 1, ncFunction);
    }


    @Override protected boolean match(long id) {

        if (super.continuousStep == 1) {
            NumberET num = super.series.get(id);
            return this.numCompareFunction.apply(num.getData(), this.threshold);
        }
        int x = 0;
        while (x < super.continuousStep) {
            NumberET num = super.series.getBefore(id, x++);
            if (!this.numCompareFunction.apply(num.getData(), this.threshold)) {
                return false;
            }
        }
        return true;
    }
}
