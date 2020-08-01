package com.github.hubble.indicator.specific;


import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.series.SeriesParams;


public class BollingBandWidthIS extends ToNumIS<TernaryNumberET> {


    public BollingBandWidthIS(SeriesParams params) {

        super(params, value -> (value.getFirst() - value.getThird()) / value.getSecond() * 100);
    }
}
