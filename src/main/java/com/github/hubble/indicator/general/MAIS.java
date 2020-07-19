package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.CacheIndicatorSeries;


public class MAIS extends CacheIndicatorSeries<NumberET, NumberET, NumberET> {


    private double sum;


    public MAIS(String name, int size, long interval, int step) {

        super(name, size, interval, step);
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        if (!super.cache.isFull()) {
            super.cache.add(ele);
            if (super.cache.isFull()) {
                this.sum = super.cache.getList().stream().mapToDouble(value -> value.getData()).sum();
                add(new NumberET(ele.getId(), this.sum / super.cache.getCapacity()));
            }
            return;
        }
        this.sum -= (updateOrInsert ? super.cache.getLast().getData() : super.cache.getFirst().getData());
        this.sum += ele.getData();
        super.cache.add(ele);
        add(new NumberET(ele.getId(), this.sum / super.cache.getCapacity()));
    }
}
