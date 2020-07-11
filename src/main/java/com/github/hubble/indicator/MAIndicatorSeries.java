package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.NumberET;


public class MAIndicatorSeries extends CacheIndicatorSeries<CandleET, NumberET, CandleET> {


    private double sum;


    public MAIndicatorSeries(String name, int size, long interval, int step) {

        super(name, size, interval, step);
    }


    @Override protected void onChange(CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        if (!isCacheFull()) {
            super.cache.add(ele);
            if (isCacheFull()) {
                this.sum = super.cache.getList().stream().mapToDouble(value -> value.getClose()).sum();
                add(new NumberET(ele.getId(), this.sum / super.cache.getCapacity()));
            }
            return;
        }

        this.sum -= (updateOrInsert ? super.cache.getLast().getClose() : super.cache.getFirst().getClose());
        this.sum += ele.getClose();
        super.cache.add(ele);
        add(new NumberET(ele.getId(), this.sum / super.cache.getCapacity()));
    }
}
