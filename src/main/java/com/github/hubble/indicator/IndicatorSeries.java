package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.SeriesListener;
import com.github.hubble.ele.Element;


public abstract class IndicatorSeries<I extends Element, R extends Element> extends Series<R> implements SeriesListener<I> {


    private long lastSequence;


    public IndicatorSeries(String name, int size, long interval) {

        super(name, size, interval);
    }


    @Override public void onChange(long sequence, I ele, boolean updateOrInsert, Series<I> series) {

        if (sequence > this.lastSequence) {
            this.lastSequence = sequence;
            onChange(ele, updateOrInsert, series);
        }
    }


    protected abstract void onChange(I ele, boolean updateOrInsert, Series<I> series);
}
