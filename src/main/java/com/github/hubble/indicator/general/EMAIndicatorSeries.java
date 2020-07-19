package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.CacheIndicatorSeries;


public class EMAIndicatorSeries extends CacheIndicatorSeries<NumberET, NumberET, NumberET> {


    private double multiplier;


    public EMAIndicatorSeries(String name, int size, long interval, int step, double multiplier) {

        super(name, size, interval, step);
        this.multiplier = multiplier;
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        if (!super.cache.isFull()) {
            super.cache.add(ele);
            if (super.cache.isFull()) {
                Double result = null;
                for (NumberET item : super.cache.getList()) {
                    result = (result == null ? item.getData() : ema(result, item));
                }
                add(new NumberET(ele.getId(), result));
            }
            return;
        }
        super.cache.add(ele);
        NumberET pre = super.getBefore(ele.getId());
        add(new NumberET(ele.getId(), ema(pre.getData(), ele)));
    }


    protected double ema(Double pre, NumberET cur) {

        double r = this.multiplier / (super.cache.getCapacity() + 1);
        return r * cur.getData() + (1 - r) * pre;
    }
}
