package com.github.hubble;


import com.github.hubble.ele.CandleET;
import com.github.hubble.indicator.MAIndicatorSeries;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class T {


    public static void main(String[] args) {

        Series<CandleET> candleETSeries = new Series<>("A", 1024, TimeUnit.MINUTES.toSeconds(1));
        MAIndicatorSeries maIndicatorSeries05 = new MAIndicatorSeries("A_MA_05", 128, TimeUnit.MINUTES.toSeconds(1), 5);
        MAIndicatorSeries maIndicatorSeries10 = new MAIndicatorSeries("A_MA_10", 128, TimeUnit.MINUTES.toSeconds(1), 10);
        MAIndicatorSeries maIndicatorSeries30 = new MAIndicatorSeries("A_MA_30", 128, TimeUnit.MINUTES.toSeconds(1), 30);
        candleETSeries.regist(maIndicatorSeries05).regist(maIndicatorSeries10).regist(maIndicatorSeries30);

        for (int i = 0; i < 100; i++) {
            CandleET candleET = new CandleET(i * 60);
            candleET.setClose(i * 1.5d);
            candleETSeries.add(candleET);
        }

        System.out.println(Arrays.toString(candleETSeries.elements));
        System.out.println(Arrays.toString(maIndicatorSeries05.elements));
        System.out.println(Arrays.toString(maIndicatorSeries10.elements));
        System.out.println(Arrays.toString(maIndicatorSeries30.elements));
    }

}
