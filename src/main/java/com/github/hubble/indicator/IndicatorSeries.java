package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.SeriesListener;
import com.github.hubble.ele.Element;


public abstract class IndicatorSeries<I extends Element, R extends Element> extends Series<R> implements SeriesListener<I> {


    public IndicatorSeries(String name, int size, long interval) {

        super(name, size, interval);
    }
}
