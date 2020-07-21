package com.github.hubble.indicator.general;


import com.github.hubble.SeriesParams;
import com.github.hubble.ele.Element;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.IndicatorSeries;
import com.github.hubble.indicator.PairIndicatorSeries;

import java.util.function.ToDoubleBiFunction;


public class CalculatePIS extends PairIndicatorSeries<IndicatorSeries<? extends Element, NumberET>, IndicatorSeries<? extends Element, NumberET>, NumberET> {


    protected ToDoubleBiFunction<NumberET, NumberET> function;


    public CalculatePIS(SeriesParams params, IndicatorSeries<? extends Element, NumberET> first, IndicatorSeries<? extends Element, NumberET> second,
                        ToDoubleBiFunction<NumberET, NumberET> function) {

        super(params, first, second);
        this.function = function;
    }


    @Override protected void onTime(long timeId) {

        NumberET one = super.first.get(timeId);
        NumberET two = super.second.get(timeId);
        if (one != null && two != null) {
            add(new NumberET(timeId, this.function.applyAsDouble(one, two)));
        }
    }
}
