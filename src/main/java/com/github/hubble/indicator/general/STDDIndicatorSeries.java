package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.CacheIndicatorSeries;


public class STDDIndicatorSeries extends CacheIndicatorSeries<NumberET, NumberET, NumberET> {


    private double sum;


    public STDDIndicatorSeries(String name, int size, long interval, int step) {

        super(name, size, interval, step);
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        if (!super.cache.isFull()) {
            super.cache.add(ele);
            if (super.cache.isFull()) {
                this.sum = super.cache.getList().stream().mapToDouble(value -> value.getData()).sum();
                calculate(ele.getId());
            }
            return;
        }
        this.sum -= (updateOrInsert ? super.cache.getLast().getData() : super.cache.getFirst().getData());
        this.sum += ele.getData();
        super.cache.add(ele);
        calculate(ele.getId());
    }


    private void calculate(long id) {

        final double avg = this.sum / super.cache.getCapacity();
        double varianceSum = super.cache.getList().stream().mapToDouble(value -> Math.pow(value.getData() - avg, 2)).sum();
        double delta = Math.sqrt(varianceSum / super.cache.getCapacity());
        add(new NumberET(id, delta));
    }
}
