package com.github.hubble.indicator.function;


import com.github.hubble.ele.TernaryNumberET;

import java.util.function.Function;


public class RSVFunction implements Function<TernaryNumberET, Double> {


    @Override public Double apply(TernaryNumberET ele) {

        return (ele.getSecond() - ele.getThird()) / (ele.getFirst() - ele.getThird()) * 100;
    }
}
