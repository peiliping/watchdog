package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.SingleET;


public class MAIndicatorSeries extends CacheIndicatorSeries<SingleET, CandleET> {


    private double last;


    public MAIndicatorSeries(String name, int size, long interval, int step) {

        super(name, size, interval, step);
    }


    @Override public void onChange(CandleET ele, boolean replace, Series<CandleET> series) {

        if (!isCacheFull()) {
            super.cache.add(ele);
            if (isCacheFull()) {
                this.last = super.cache.getList().stream().mapToDouble(value -> value.getClose()).sum();
                add(new SingleET(ele.getId(), this.last / super.cache.getCapacity()));
            }
            return;
        }

        this.last -= (replace ? super.cache.getLast().getClose() : super.cache.getFirst().getClose());
        this.last += ele.getClose();
        super.cache.add(ele);
        add(new SingleET(ele.getId(), this.last / super.cache.getCapacity()));
    }
}
