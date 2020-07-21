package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.SeriesParams;
import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.PairIndicatorSeries;


public class BollingPIS extends PairIndicatorSeries<NumberET, TernaryNumberET, NumberET> {


    private double multiplier;


    public BollingPIS(SeriesParams params, double multiplier, STDDIS stdd, MAIS ma) {

        super(params, stdd, ma);
        this.multiplier = multiplier;
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        NumberET stdd = super.first.get(ele.getId());
        NumberET ma = super.second.get(ele.getId());
        if (stdd != null && ma != null) {
            double delta = stdd.getData() * this.multiplier;
            add(new TernaryNumberET(ele.getId(), ma.getData() + delta, ma.getData(), ma.getData() - delta));
        }
    }
}
