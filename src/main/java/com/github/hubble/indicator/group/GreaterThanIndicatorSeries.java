package com.github.hubble.indicator.group;


import com.github.hubble.Series;
import com.github.hubble.ele.CustomCompare;
import com.github.hubble.ele.Element;
import com.github.hubble.ele.BooleanET;
import com.github.hubble.indicator.IndicatorSeries;


public class GreaterThanIndicatorSeries<I extends Element, SR extends Element> extends PairIndicatorSeries<I, BooleanET, SR> {


    private CustomCompare<SR> customCompare;


    public GreaterThanIndicatorSeries(String name, int size, long interval, IndicatorSeries<I, SR> first, IndicatorSeries<I, SR> second,
                                      CustomCompare<SR> customCompare) {

        super(name, size, interval, first, second);
        this.customCompare = customCompare;
    }


    @Override public void onChange(I ele, boolean replace, Series<I> series) {

        super.onChange(ele, replace, series);
        SR sr1 = super.first.getLast();
        SR sr2 = super.second.getLast();
        if (sr1 != null && sr2 != null) {
            int r = customCompare.compareWith(sr1, sr2);
            add(new BooleanET(ele.getId(), r > 0));
        }
    }
}
