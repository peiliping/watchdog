package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.IndicatorSeries;


public class KDJIS extends IndicatorSeries<NumberET, TernaryNumberET> {


    public KDJIS(String name, int size, long interval) {

        super(name, size, interval);
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        TernaryNumberET last = super.getBefore(ele.getId());
        if (last == null) {
            last = new TernaryNumberET(ele.getId(), 50, 50, 0);
        }
        double k = (last.getHigh() * 2) / 3 + ele.getData() / 3;
        double d = (last.getMiddle() * 2) / 3 + k / 3;
        double j = k * 3 - d * 2;
        add(new TernaryNumberET(ele.getId(), k, d, j));
    }
}