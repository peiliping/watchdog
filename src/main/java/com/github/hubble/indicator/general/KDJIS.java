package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.PairIndicatorSeries;


public class KDJIS extends PairIndicatorSeries<NumberET, TernaryNumberET, NumberET> {


    public KDJIS(String name, int size, long interval, MAIS k, MAIS d) {

        super(name, size, interval, k, d);
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        NumberET k = super.first.get(ele.getId());
        NumberET d = super.second.get(ele.getId());
        if (k != null && d != null) {
            add(new TernaryNumberET(ele.getId(), k.getData(), d.getData(), 3 * k.getData() - 2 * d.getData()));
        }
    }
}
