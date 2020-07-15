package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.NumberET;

import java.util.function.Function;


public class PriceIndicatorSeries extends IndicatorSeries<CandleET, NumberET> {


    private Function<CandleET, Double> function;


    public PriceIndicatorSeries(String name, int size, long interval, Function<CandleET, Double> function) {

        super(name, size, interval);
        this.function = function;
    }


    @Override protected void onChange(CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        add(new NumberET(ele.getId(), this.function.apply(ele)));
    }
}
