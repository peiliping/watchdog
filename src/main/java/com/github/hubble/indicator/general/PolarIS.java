package com.github.hubble.indicator.general;


import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.CacheIndicatorSeries;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesParams;


public class PolarIS extends CacheIndicatorSeries<CandleET, TernaryNumberET, CandleET> {


    public PolarIS(SeriesParams params, int step) {

        super(params, step);
    }


    @Override protected void onChange(CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        super.cache.add(ele);
        if (super.cache.isFull()) {
            add(convert(ele));
        }
    }


    private TernaryNumberET convert(CandleET ele) {

        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        for (CandleET candleET : super.cache.getList()) {
            max = Math.max(max, candleET.getHigh());
            min = Math.min(min, candleET.getLow());
        }
        return new TernaryNumberET(ele.getId(), max, ele.getClose(), min);
    }
}
