package com.github.hubble.indicator.specific;


import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.PairIndicatorSeries;
import com.github.hubble.indicator.general.MAIS;
import com.github.hubble.series.SeriesParams;


public class KDJPIS extends PairIndicatorSeries<MAIS, MAIS, TernaryNumberET> {


    public KDJPIS(SeriesParams params, MAIS k, MAIS d) {

        super(params, k, d);
    }


    @Override protected void onTime(long timeSeq) {

        NumberET k = super.first.get(timeSeq);
        NumberET d = super.second.get(timeSeq);
        if (k != null && d != null) {
            add(new TernaryNumberET(timeSeq, k.getData(), d.getData(), 3 * k.getData() - 2 * d.getData()));
        }
    }
}
