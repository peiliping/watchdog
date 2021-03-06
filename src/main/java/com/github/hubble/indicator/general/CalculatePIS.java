package com.github.hubble.indicator.general;


import com.github.hubble.ele.Element;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.IndicatorSeries;
import com.github.hubble.indicator.PairIndicatorSeries;
import com.github.hubble.series.SeriesParams;

import java.util.function.ToDoubleBiFunction;


public class CalculatePIS extends PairIndicatorSeries<IndicatorSeries<? extends Element, NumberET>, IndicatorSeries<? extends Element, NumberET>, NumberET> {


    public static final ToDoubleBiFunction<NumberET, NumberET> PLUS = (num1, num2) -> num1.getData() + num2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> MINUS = (num1, num2) -> num1.getData() - num2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> MULTI = (num1, num2) -> num1.getData() * num2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> DIVIDE = (num1, num2) -> num1.getData() / num2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> PERCENT = (num1, num2) -> num1.getData() / num2.getData() * 100;

    protected final ToDoubleBiFunction<NumberET, NumberET> function;


    public CalculatePIS(SeriesParams params, IndicatorSeries<? extends Element, NumberET> first, IndicatorSeries<? extends Element, NumberET> second,
                        ToDoubleBiFunction<NumberET, NumberET> function) {

        super(params, first, second);
        this.function = function;
    }


    @Override protected void onTime(long timeSeq) {

        NumberET one = super.first.get(timeSeq);
        NumberET two = super.second.get(timeSeq);
        if (one != null && two != null) {
            add(new NumberET(timeSeq, this.function.applyAsDouble(one, two)));
        }
    }
}
