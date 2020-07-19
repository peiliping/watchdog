package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.HMLNumber;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.pair.PairIndicatorSeries;


public class BollingIndicatorSeries extends PairIndicatorSeries<NumberET, HMLNumber, NumberET> {


    private double multiplier;


    public BollingIndicatorSeries(String name, int size, long interval, double multiplier, STDDIndicatorSeries stdd, MAIndicatorSeries ma) {

        super(name, size, interval, stdd, ma);
        this.multiplier = multiplier;
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        NumberET stdd = super.first.get(ele.getId());
        NumberET ma = super.second.get(ele.getId());
        if (stdd != null && ma != null) {
            double delta = stdd.getData() * this.multiplier;
            add(new HMLNumber(ele.getId(), ma.getData() + delta, ma.getData(), ma.getData() - delta));
        }
    }
}
