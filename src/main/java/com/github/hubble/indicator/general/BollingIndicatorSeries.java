package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.HMLNumber;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.IndicatorSeries;


public class BollingIndicatorSeries extends IndicatorSeries<CandleET, HMLNumber> {


    private double multiplier;

    private STDDIndicatorSeries stddIndicatorSeries;

    private MAIndicatorSeries maIndicatorSeries;


    public BollingIndicatorSeries(String name, int size, long interval, double multiplier, STDDIndicatorSeries stdd, MAIndicatorSeries ma) {

        super(name, size, interval);
        this.multiplier = multiplier;
        this.stddIndicatorSeries = stdd;
        this.maIndicatorSeries = ma;
    }


    @Override protected void onChange(CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        NumberET stdd = this.stddIndicatorSeries.get(ele.getId());
        NumberET ma = this.maIndicatorSeries.get(ele.getId());
        if (stdd != null && ma != null) {
            double delta = stdd.getData() * this.multiplier;
            add(new HMLNumber(ele.getId(), ma.getData() + delta, ma.getData(), ma.getData() - delta));
        }
    }
}
