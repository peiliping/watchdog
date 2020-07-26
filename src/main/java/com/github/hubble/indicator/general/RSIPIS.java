package com.github.hubble.indicator.general;


import com.github.hubble.indicator.function.PISFuncs;
import com.github.hubble.series.SeriesParams;


public class RSIPIS extends CalculatePIS {


    public RSIPIS(SeriesParams params, EMAIS up, EMAIS total) {

        super(params, up, total, PISFuncs.PERCENT);
    }

}
