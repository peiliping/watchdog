package com.github.hubble.rule.series.cross;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.series.Series;


public class RisingCrossPSR extends CrossPSR {


    public RisingCrossPSR(String name, Series<NumberET> first, Series<NumberET> second) {

        super(name, first, second);
        super.current = NumCompareFunction.GT;
        super.before1 = NumCompareFunction.LTE;
        super.before2 = NumCompareFunction.LT;
    }
}
