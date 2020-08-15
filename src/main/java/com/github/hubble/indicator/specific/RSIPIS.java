package com.github.hubble.indicator.specific;


import com.github.hubble.indicator.PairIndicatorSeriesFuncs;
import com.github.hubble.indicator.general.CalculatePIS;
import com.github.hubble.indicator.general.EMAIS;
import com.github.hubble.series.SeriesParams;


public class RSIPIS extends CalculatePIS {


    public RSIPIS(SeriesParams params, EMAIS up, EMAIS total) {

        super(params, up, total, PairIndicatorSeriesFuncs.PERCENT);
    }

}
