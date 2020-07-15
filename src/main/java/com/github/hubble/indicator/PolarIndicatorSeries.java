package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;


public class PolarIndicatorSeries extends CacheIndicatorSeries<CandleET, CandleET, CandleET> {


    public PolarIndicatorSeries(String name, int size, long interval, int cacheSize) {

        super(name, size, interval, cacheSize);
    }


    @Override protected void onChange(CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        super.cache.add(ele);
        if (!isCacheFull()) {
            return;
        }
        add(convert(ele));
    }


    private CandleET convert(CandleET ele) {

        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        for (CandleET candleET : super.cache.getList()) {
            max = Math.max(max, candleET.getHigh());
            min = Math.min(min, candleET.getLow());
        }
        CandleET candleET = new CandleET(ele.getId());
        candleET.setOpen(ele.getOpen());
        candleET.setLow(min);
        candleET.setHigh(max);
        candleET.setClose(ele.getClose());
        candleET.setAmount(ele.getAmount());
        candleET.setVolume(ele.getVolume());
        candleET.setCount(ele.getCount());
        return candleET;
    }
}
