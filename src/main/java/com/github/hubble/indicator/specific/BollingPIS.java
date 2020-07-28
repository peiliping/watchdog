package com.github.hubble.indicator.specific;


import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.PairIndicatorSeries;
import com.github.hubble.indicator.general.MAIS;
import com.github.hubble.indicator.general.STDDIS;
import com.github.hubble.series.SeriesParams;


public class BollingPIS extends PairIndicatorSeries<STDDIS, MAIS, TernaryNumberET> {


    private double multiplier;


    public BollingPIS(SeriesParams params, double multiplier, STDDIS stdd, MAIS ma) {

        super(params, stdd, ma);
        this.multiplier = multiplier;
    }


    @Override protected void onTime(long timeSeq) {

        NumberET stdd = super.first.get(timeSeq);
        NumberET ma = super.second.get(timeSeq);
        if (stdd != null && ma != null) {
            double delta = stdd.getData() * this.multiplier;
            add(new TernaryNumberET(timeSeq, ma.getData() + delta, ma.getData(), ma.getData() - delta));
        }
    }
}
