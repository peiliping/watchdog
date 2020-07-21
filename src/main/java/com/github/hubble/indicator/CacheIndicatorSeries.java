package com.github.hubble.indicator;


import com.github.hubble.SeriesParams;
import com.github.hubble.ele.Element;
import lombok.Getter;


public abstract class CacheIndicatorSeries<I extends Element, R extends Element, C extends Element> extends IndicatorSeries<I, R> {


    @Getter
    protected LastNQueue<C> cache;


    public CacheIndicatorSeries(SeriesParams params, int cacheSize) {

        super(params);
        this.cache = new LastNQueue<>(cacheSize);
    }
}
