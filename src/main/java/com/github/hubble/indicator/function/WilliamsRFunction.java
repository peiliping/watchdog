package com.github.hubble.indicator.function;


import com.github.hubble.ele.TernaryNumberET;

import java.util.function.ToDoubleFunction;


public class WilliamsRFunction implements ToDoubleFunction<TernaryNumberET> {


    @Override public double applyAsDouble(TernaryNumberET ele) {

        return (ele.getFirst() - ele.getSecond()) / (ele.getFirst() - ele.getThird()) * 100;
    }
}
