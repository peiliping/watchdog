package com.github.hubble.indicator.general;


import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.PairIndicatorSeries;
import com.github.hubble.series.SeriesParams;


public class KDJIS extends PairIndicatorSeries<MAIS, MAIS, TernaryNumberET> {


    public KDJIS(SeriesParams params, MAIS k, MAIS d) {

        super(params, k, d);
    }


    @Override protected void onTime(long timeId) {

        NumberET k = super.first.get(timeId);
        NumberET d = super.second.get(timeId);
        if (k != null && d != null) {
            add(new TernaryNumberET(timeId, k.getData(), d.getData(), 3 * k.getData() - 2 * d.getData()));
        }
    }
}
