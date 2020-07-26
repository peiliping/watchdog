package com.github.hubble.indicator.general;


import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.series.SeriesParams;


public class WRIS extends ToNumIS<TernaryNumberET> {


    public WRIS(SeriesParams params) {

        super(params, ele -> (ele.getFirst() - ele.getSecond()) / (ele.getFirst() - ele.getThird()) * 100);
    }
}
