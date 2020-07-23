package com.github.hubble.indicator;


import com.github.hubble.ele.Element;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesParams;
import com.github.hubble.series.SeriesUpsertListener;
import org.apache.commons.lang3.Validate;


public abstract class IndicatorSeries<I extends Element, R extends Element> extends Series<R> implements SeriesUpsertListener<I> {


    protected long lastSequence;

    protected Series parentSeries;


    public IndicatorSeries(SeriesParams params) {

        super(params);
    }


    public void after(Series<I> series) {

        Validate.isTrue(this.parentSeries == null);
        this.parentSeries = series;
        super.parentName = series.getFullName();
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
