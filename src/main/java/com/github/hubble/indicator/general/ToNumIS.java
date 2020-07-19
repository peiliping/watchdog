package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.Element;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.IndicatorSeries;

import java.util.function.Function;


public class ToNumIS<I extends Element> extends IndicatorSeries<I, NumberET> {


    protected Function<I, Double> function;


    public ToNumIS(String name, int size, long interval, Function<I, Double> function) {

        super(name, size, interval);
        this.function = function;
    }


    @Override protected void onChange(I ele, boolean updateOrInsert, Series<I> series) {

        add(new NumberET(ele.getId(), this.function.apply(ele)));
    }
}
