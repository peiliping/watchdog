package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.SeriesListener;
import com.github.hubble.ele.Element;


public abstract class IndicatorSeries<I extends Element, R extends Element> extends Series<R> implements SeriesListener<I> {


    protected long lastSequence;


    public IndicatorSeries(String name, int size, long interval) {

        super(name, size, interval);
    }


    public IndicatorSeries<I, R> after(Series<I>... series) {

        for (Series<I> t : series) {
            t.bind(this);
        }
        return this;
    }


    @Override public void onChange(long seq, I ele, boolean updateOrInsert, Series<I> series) {

        if (seq > this.lastSequence) {
            this.lastSequence = seq;
            onChange(ele, updateOrInsert, series);
        }
    }


    protected abstract void onChange(I ele, boolean updateOrInsert, Series<I> series);
}
