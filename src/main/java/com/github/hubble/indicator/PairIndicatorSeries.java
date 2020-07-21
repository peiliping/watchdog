package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.SeriesParams;
import com.github.hubble.SeriesTimeListener;
import com.github.hubble.ele.Element;
import com.github.hubble.ele.NumberET;
import org.apache.commons.lang3.Validate;


public abstract class PairIndicatorSeries<F extends IndicatorSeries, S extends IndicatorSeries, R extends Element>
        extends IndicatorSeries<NumberET, R> implements SeriesTimeListener {


    protected F first;

    protected S second;


    public PairIndicatorSeries(SeriesParams params, F first, S second) {

        super(params);
        this.first = first;
        this.second = second;
    }


    @Override public void after(Series series) {

        series.bindTimeListener(this);
    }


    @Override public final void onChange(long seq, NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        Validate.isTrue(false);
    }


    @Override protected final void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

    }


    @Override public void onTime(long seq, long timeId) {

        if (seq > super.lastSequence) {
            super.lastSequence = seq;
            onTime(timeId);
        }
    }


    protected abstract void onTime(long timeId);
}
