package com.github.hubble.indicator.specific;


import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.PairIndicatorSeries;
import com.github.hubble.indicator.general.CalculatePIS;
import com.github.hubble.indicator.general.EMAIS;
import com.github.hubble.series.SeriesParams;


public class MACDPIS extends PairIndicatorSeries<CalculatePIS, EMAIS, TernaryNumberET> {


    public MACDPIS(SeriesParams params, CalculatePIS dif, EMAIS dea) {

        super(params, dif, dea);
    }


    @Override protected void onTime(long timeSeq) {

        NumberET dif = super.first.get(timeSeq);
        NumberET dea = super.second.get(timeSeq);
        if (dif != null && dea != null) {
            add(new TernaryNumberET(timeSeq, dif.getData(), dea.getData(), dif.getData() - dea.getData()));
        }
    }
}
