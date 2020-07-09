package com.github.hubble.indicator;


import com.github.hubble.LastNQueue;
import com.github.hubble.ele.Element;
import com.github.hubble.ele.SingleET;


public abstract class CacheIndicatorSeries<R extends Element> extends IndicatorSeries<R> {


    protected int step;

    protected LastNQueue<SingleET> cache;


    public CacheIndicatorSeries(String name, int size, long interval, int step) {

        super(name, size, interval);
        this.step = step;
        this.cache = new LastNQueue<>(step);
    }
}
