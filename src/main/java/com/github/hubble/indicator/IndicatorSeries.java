package com.github.hubble.indicator;


import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.Element;
import com.github.hubble.Series;
import com.github.hubble.SeriesListener;


public abstract class IndicatorSeries<R extends Element> extends Series<R> implements SeriesListener<CandleET> {


    public IndicatorSeries(String name, int size, long interval) {

        super(name, size, interval);
    }
}
