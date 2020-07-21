package com.github.hubble.indicator.function;


import com.github.hubble.ele.TernaryNumberET;

import java.util.function.ToDoubleFunction;


public class RSVFunction implements ToDoubleFunction<TernaryNumberET> {


    @Override public double applyAsDouble(TernaryNumberET ele) {

        return (ele.getSecond() - ele.getThird()) / (ele.getFirst() - ele.getThird()) * 100;
    }
}
