package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.ele.Element;


public abstract class PairIndicatorSeries<I extends Element, R extends Element, SR extends Element> extends IndicatorSeries<I, R> {


    protected IndicatorSeries<I, SR> first;

    protected IndicatorSeries<I, SR> second;

    protected boolean broadcast = true;


    public PairIndicatorSeries(String name, int size, long interval, IndicatorSeries<I, SR> first, IndicatorSeries<I, SR> second) {

        super(name, size, interval);
        this.first = first;
        this.second = second;
    }


    @Override public void onChange(long seq, I ele, boolean updateOrInsert, Series<I> series) {

        if (seq > super.lastSequence) {
            super.lastSequence = seq;
            if (this.broadcast) {
                this.first.onChange(seq, ele, updateOrInsert, series);
                this.second.onChange(seq, ele, updateOrInsert, series);
            }
            onChange(ele, updateOrInsert, series);
        }
    }
}
