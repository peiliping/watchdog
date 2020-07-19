package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.HMLNumber;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.PairIndicatorSeries;


public class BollingPIS extends PairIndicatorSeries<NumberET, HMLNumber, NumberET> {


    private double multiplier;


    public BollingPIS(String name, int size, long interval, double multiplier, STDDIS stdd, MAIS ma) {

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
