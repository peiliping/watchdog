package com.github.hubble.indicator.general;


import com.github.hubble.Series;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.IndicatorSeries;
import com.github.hubble.indicator.PairIndicatorSeries;

import java.util.function.ToDoubleBiFunction;


public class CalculatePIS extends PairIndicatorSeries<NumberET, NumberET, NumberET> {


    protected ToDoubleBiFunction<NumberET, NumberET> function;


    public CalculatePIS(String name, int size, long interval, IndicatorSeries<NumberET, NumberET> first, IndicatorSeries<NumberET, NumberET> second,
                        ToDoubleBiFunction<NumberET, NumberET> function) {

        super(name, size, interval, first, second);
        this.function = function;
    }


    @Override protected void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        NumberET one = super.first.get(ele.getId());
        NumberET two = super.second.get(ele.getId());
        if (one != null && two != null) {
            add(new NumberET(ele.getId(), this.function.applyAsDouble(one, two)));
        }
    }
}
