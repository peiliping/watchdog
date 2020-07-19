package com.github.hubble.indicator.function;


import com.github.hubble.ele.TernaryNumberET;

import java.util.function.Function;


public class RSVFunction implements Function<TernaryNumberET, Double> {


    @Override public Double apply(TernaryNumberET source) {

        return ((source.getMiddle() - source.getLow()) / (source.getHigh() - source.getLow())) * 100;
    }
}
