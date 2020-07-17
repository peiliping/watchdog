package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.HMLNumber;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.CacheIndicatorSeries;


public class BollingIndicatorSeries extends CacheIndicatorSeries<NumberET, HMLNumber, NumberET> {


    private double multiplier;

    private double maSum;


    public BollingIndicatorSeries(String name, int size, long interval, int step, double multiplier) {

        super(name, size, interval, step);
        this.multiplier = multiplier;
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        if (!isCacheFull()) {
            super.cache.add(ele);
            if (isCacheFull()) {
                this.maSum = super.cache.getList().stream().mapToDouble(value -> value.getData()).sum();
                calculate(ele, series.get(ele.getId() - series.getInterval()));
            }
            return;
        }
        this.maSum -= (updateOrInsert ? super.cache.getLast().getData() : super.cache.getFirst().getData());
        this.maSum += ele.getData();
        super.cache.add(ele);
        calculate(ele, series.get(ele.getId() - series.getInterval()));
    }


    private void calculate(NumberET ele, NumberET pre) {

        final double avg = this.maSum / super.cache.getCapacity();
        double varianceSum = super.cache.getList().stream().mapToDouble(value -> Math.pow(value.getData() - avg, 2)).sum();
        double delta = Math.sqrt(varianceSum / super.cache.getCapacity()) * this.multiplier;
        add(new HMLNumber(ele.getId(), pre.getData() + delta, ele.getData(), pre.getData() - delta));
    }
}
