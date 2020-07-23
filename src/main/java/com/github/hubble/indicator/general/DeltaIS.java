package com.github.hubble.indicator.general;


import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.CacheIndicatorSeries;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesParams;


public class DeltaIS extends CacheIndicatorSeries<NumberET, NumberET, NumberET> {


    public DeltaIS(SeriesParams params) {

        super(params, 2);
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        super.cache.add(ele);
        if (super.cache.isFull()) {
            add(new NumberET(ele.getId(), ele.getData() - super.cache.getFirst().getData()));
        }
    }
}
