package com.github.hubble.indicator.general;


import com.github.hubble.ele.Element;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.IndicatorSeries;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesParams;


public class ConstIS<I extends Element> extends IndicatorSeries<I, NumberET> {


    private double value;


    public ConstIS(SeriesParams params, double value) {

        super(params);
        this.value = value;
    }


    @Override protected void onChange(I ele, boolean updateOrInsert, Series<I> series) {

        add(new NumberET(ele.getId(), this.value));
    }
}
