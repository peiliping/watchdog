package com.github.hubble.indicator.function;


import com.github.hubble.ele.HMLNumber;

import java.util.function.Function;


public class RSVFunction implements Function<HMLNumber, Double> {


    @Override public Double apply(HMLNumber source) {

        return (source.getMiddle() - source.getLow()) / (source.getHigh() - source.getLow()) * 100;
    }
}
