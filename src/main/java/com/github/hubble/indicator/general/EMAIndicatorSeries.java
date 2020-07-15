package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.CacheIndicatorSeries;


public class EMAIndicatorSeries extends CacheIndicatorSeries<CandleET, NumberET, NumberET> {


    private double multiplier;


    public EMAIndicatorSeries(String name, int size, long interval, int step, double multiplier) {

        super(name, size, interval, step);
        this.multiplier = multiplier;
    }


    @Override protected void onChange(CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        NumberET cur = new NumberET(ele.getId(), ele.getClose());
        if (super.cache.size() == 0 || (super.cache.size() == 1 && updateOrInsert)) {
            super.cache.add(cur);
            return;
        }

        NumberET ema = new NumberET(ele.getId(), ema(super.cache.getLast(), cur));
        super.cache.add(ema);
        if (isCacheFull()) {
            add(ema);
        }
    }


    protected double ema(NumberET pre, NumberET cur) {

        return pre.getData() + (cur.getData() - pre.getData()) * this.multiplier;
    }
}
