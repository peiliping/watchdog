package com.github.hubble.rule.series.cross;


import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.NumberET;
import com.github.hubble.series.Series;


public class FallingCrossPSR extends CrossPSR {


    public FallingCrossPSR(String name, Series<NumberET> first, Series<NumberET> second) {

        super(name, first, second);
        super.current = NumCompareFunction.LT;
        super.before1 = NumCompareFunction.GTE;
        super.before2 = NumCompareFunction.GT;
    }
}
