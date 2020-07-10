package com.github.hubble.indicator;


import com.github.hubble.ele.Element;
import lombok.Getter;


public abstract class CacheIndicatorSeries<R extends Element, C extends Element> extends IndicatorSeries<R> {


    @Getter
    protected LastNQueue<C> cache;


    public CacheIndicatorSeries(String name, int size, long interval, int cacheSize) {

        super(name, size, interval);
        this.cache = new LastNQueue<>(cacheSize);
    }


    protected boolean isCacheFull() {

        return this.cache.getList().size() == this.cache.getCapacity();
    }
}
