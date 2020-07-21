package com.github.hubble.indicator.general;


import com.github.hubble.SeriesParams;
import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.PairIndicatorSeries;


public class BollingPIS extends PairIndicatorSeries<STDDIS, MAIS, TernaryNumberET> {


    private double multiplier;


    public BollingPIS(SeriesParams params, double multiplier, STDDIS stdd, MAIS ma) {

        super(params, stdd, ma);
        this.multiplier = multiplier;
    }


    @Override protected void onTime(long timeId) {

        NumberET stdd = super.first.get(timeId);
        NumberET ma = super.second.get(timeId);
        if (stdd != null && ma != null) {
            double delta = stdd.getData() * this.multiplier;
            add(new TernaryNumberET(timeId, ma.getData() + delta, ma.getData(), ma.getData() - delta));
        }
    }
}
