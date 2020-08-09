package com.github.watchdog.task.hb;


import com.github.hubble.common.CandleType;
import com.google.common.collect.Sets;

import java.util.Set;


public class Candles {


    public static final Set<CandleType> candles = Sets.newHashSet();

    static {
        candles.add(CandleType.MIN_1);
        candles.add(CandleType.MIN_15);
        candles.add(CandleType.MIN_30);
    }

}
