package com.github.hubble.indicator;


import com.github.hubble.ele.Element;
import com.github.hubble.series.SeriesParams;
import lombok.Getter;


public abstract class CacheIndicatorSeries<I extends Element, R extends Element, C extends Element> extends IndicatorSeries<I, R> {


    @Getter
    protected final LastNQueue<C> cache;


    public CacheIndicatorSeries(SeriesParams params, int cacheSize) {

        super(params);
        this.cache = new LastNQueue<>(cacheSize);
    }
}
