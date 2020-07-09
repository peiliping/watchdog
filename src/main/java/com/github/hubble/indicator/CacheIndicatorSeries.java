package com.github.hubble.indicator;


import com.github.hubble.ele.Element;


public abstract class CacheIndicatorSeries<R extends Element> extends IndicatorSeries<R> {


    protected int step;

    protected LastNQueue<R> cache;


    public CacheIndicatorSeries(String name, int size, long interval, int step) {

        super(name, size, interval);
        this.step = step;
        this.cache = new LastNQueue<>(step);
    }
}
