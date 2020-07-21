package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.SeriesParams;
import com.github.hubble.ele.Element;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.IndicatorSeries;

import java.util.function.ToDoubleFunction;


public class ToNumIS<I extends Element> extends IndicatorSeries<I, NumberET> {


    protected ToDoubleFunction<I> function;


    public ToNumIS(SeriesParams params, ToDoubleFunction<I> function) {

        super(params);
        this.function = function;
    }


    @Override protected void onChange(I ele, boolean updateOrInsert, Series<I> series) {

        add(new NumberET(ele.getId(), this.function.applyAsDouble(ele)));
    }
}
