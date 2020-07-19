package com.github.hubble.indicator.other;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.HMLNumber;
import com.github.hubble.indicator.CacheIndicatorSeries;


public class PolarIndicatorSeries extends CacheIndicatorSeries<CandleET, HMLNumber, CandleET> {


    public PolarIndicatorSeries(String name, int size, long interval, int step) {

        super(name, size, interval, step);
    }


    @Override protected void onChange(CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        super.cache.add(ele);
        if (super.cache.isFull()) {
            add(convert(ele));
        }
    }


    private HMLNumber convert(CandleET ele) {

        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        for (CandleET candleET : super.cache.getList()) {
            max = Math.max(max, candleET.getHigh());
            min = Math.min(min, candleET.getLow());
        }
        return new HMLNumber(ele.getId(), max, ele.getClose(), min);
    }
}
