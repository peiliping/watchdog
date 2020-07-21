package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.SeriesParams;
import com.github.hubble.SeriesUpsertListener;
import com.github.hubble.ele.Element;


public abstract class IndicatorSeries<I extends Element, R extends Element> extends Series<R> implements SeriesUpsertListener<I> {


    protected long lastSequence;


    public IndicatorSeries(SeriesParams params) {

        super(params);
    }


    public void after(Series<I> series) {

        series.bindUpsertListener(this);
    }


    @Override public void onChange(long seq, I ele, boolean updateOrInsert, Series<I> series) {

        if (seq > this.lastSequence) {
            this.lastSequence = seq;
            onChange(ele, updateOrInsert, series);
        }
    }


    protected abstract void onChange(I ele, boolean updateOrInsert, Series<I> series);
}
