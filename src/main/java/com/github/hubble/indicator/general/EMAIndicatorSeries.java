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

        if (super.cache.size() == 0 || (super.cache.size() == 1 && updateOrInsert)) {
            super.cache.add(new NumberET(ele.getId(), ele.getVal()));
            return;
        }

        NumberET ema = new NumberET(ele.getId(), ema(updateOrInsert ? super.cache.getLast(2) : super.cache.getLast(), ele.getVal()));
        super.cache.add(ema);
        if (isCacheFull()) {
            add(ema);
        }
    }


    protected double ema(NumberET pre, double cur) {

        double r = this.multiplier / (super.cache.getCapacity() + 1);
        return r * cur + (1 - r) * pre.getData();
    }
}
