package com.github.hubble.rule.series.direction;


import com.github.hubble.Series;
import com.github.hubble.ele.CustomCompare;
import com.github.hubble.ele.Element;
import com.github.hubble.rule.RuleResult;
import com.github.hubble.rule.series.SeriesRule;

import java.util.List;


public class DirectionalSeriesRule<E extends Element> extends SeriesRule<E> {


    protected double ratio;

    protected int step;

    protected CustomCompare<E> customCompare;


    public DirectionalSeriesRule(String name, Series<E> series, double ratio, int step, CustomCompare<E> customCompare) {

        super(name, series);
        this.ratio = ratio;
        this.step = step;
        this.customCompare = customCompare;
    }


    @Override protected boolean match(long id, List<RuleResult> results) {

        double c = 0, m = 0;
        E first = null;
        E last = null;
        for (long i = id - (this.step - 1) * super.series.getInterval(); i <= id; i += super.series.getInterval()) {
            E e = super.series.get(i);
            if (e != null) {
                c++;
                if (first == null) {
                    first = e;
                }
                if (last != null && this.customCompare.exec(e, last)) {
                    m++;
                }
            }
            last = e;
        }
        return (m / c) >= this.ratio && this.customCompare.exec(last, first);
    }
}
