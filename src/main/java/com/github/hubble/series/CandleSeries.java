package com.github.hubble.series;


import com.github.hubble.ele.CandleET;


public class CandleSeries extends Series<CandleET> {


    public CandleSeries(SeriesParams params, String parentName) {

        super(params);
        super.parentName = parentName;
    }
}
