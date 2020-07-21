package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.PairIndicatorSeries;


public class MACDPIS extends PairIndicatorSeries<NumberET, TernaryNumberET, NumberET> {


    public MACDPIS(String name, int size, long interval, CalculatePIS dif, EMAIS dea) {

        super(name, size, interval, dif, dea);
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        NumberET dif = super.first.get(ele.getId());
        NumberET dea = super.second.get(ele.getId());
        if (dif != null && dea != null) {
            add(new TernaryNumberET(ele.getId(), dif.getData(), dea.getData(), dif.getData() - dea.getData()));
        }
    }
}
