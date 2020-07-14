package com.github.hubble.indicator;


import com.github.hubble.Series;
import com.github.hubble.ele.Element;
import com.github.hubble.ele.NumberET;


public class ConstantIndicatorSeries<I extends Element> extends IndicatorSeries<I, NumberET> {


    private double value;


    public ConstantIndicatorSeries(String name, int size, long interval, double value) {

        super(name, size, interval);
        this.value = value;
    }


    @Override protected void onChange(I ele, boolean updateOrInsert, Series<I> series) {

        add(new NumberET(ele.getId(), this.value));
    }
}
