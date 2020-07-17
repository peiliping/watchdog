package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.ele.Element;
import com.github.hubble.ele.NumberET;

import java.util.function.Function;


public class ToNumIndicatorSeries extends IndicatorSeries<Element, NumberET> {


    private Function<Element, Double> function;


    public ToNumIndicatorSeries(String name, int size, long interval, Function<Element, Double> function) {

        super(name, size, interval);
        this.function = function;
    }


    @Override protected void onChange(Element ele, boolean updateOrInsert, Series<Element> series) {

        add(new NumberET(ele.getId(), this.function.apply(ele)));
    }
}
