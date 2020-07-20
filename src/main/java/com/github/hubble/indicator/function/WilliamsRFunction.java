package com.github.hubble.indicator.function;


import com.github.hubble.ele.TernaryNumberET;

import java.util.function.Function;


public class WilliamsRFunction implements Function<TernaryNumberET, Double> {


    @Override public Double apply(TernaryNumberET ele) {

        return (ele.getFirst() - ele.getSecond()) / (ele.getFirst() - ele.getThird()) * 100;
    }
}
