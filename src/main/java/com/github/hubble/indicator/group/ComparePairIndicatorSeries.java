package com.github.hubble.indicator.group;


import com.github.hubble.Series;
import com.github.hubble.ele.BooleanET;
import com.github.hubble.ele.CustomCompare;
import com.github.hubble.ele.Element;
import com.github.hubble.indicator.IndicatorSeries;


public class ComparePairIndicatorSeries<I extends Element, SR extends Element> extends PairIndicatorSeries<I, BooleanET, SR> {


    private CustomCompare<SR> customCompare;


    public ComparePairIndicatorSeries(String name, int size, long interval, IndicatorSeries<I, SR> first, IndicatorSeries<I, SR> second,
                                      CustomCompare<SR> customCompare) {

        super(name, size, interval, first, second);
        this.customCompare = customCompare;
    }


    @Override public void onChange(I ele, boolean updateOrInsert, Series<I> series) {

        SR sr1 = super.first.get(ele.getId());
        SR sr2 = super.second.get(ele.getId());
        if (sr1 != null && sr2 != null) {
            add(new BooleanET(ele.getId(), this.customCompare.exec(sr1, sr2)));
        }
    }
}
