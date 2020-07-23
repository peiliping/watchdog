package com.github.hubble.indicator.general;


import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.PairIndicatorSeries;
import com.github.hubble.series.SeriesParams;


public class MACDPIS extends PairIndicatorSeries<CalculatePIS, EMAIS, TernaryNumberET> {


    public MACDPIS(SeriesParams params, CalculatePIS dif, EMAIS dea) {

        super(params, dif, dea);
    }


    @Override protected void onTime(long timeId) {

        NumberET dif = super.first.get(timeId);
        NumberET dea = super.second.get(timeId);
        if (dif != null && dea != null) {
            add(new TernaryNumberET(timeId, dif.getData(), dea.getData(), dif.getData() - dea.getData()));
        }
    }
}
