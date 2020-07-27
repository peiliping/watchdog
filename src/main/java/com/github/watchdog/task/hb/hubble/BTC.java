package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.AbstractHubbleWithCommonRL;
import com.github.hubble.common.CandleType;


public class BTC extends AbstractHubbleWithCommonRL {


    public BTC(String market, String name) {

        super(market, name);
        super.shockRatio = 0.8d;
    }


    @Override public AbstractHubble init() {

        initShockRL();
        initMARL(CandleType.MIN_60, 1800);
        initWRRL(CandleType.MIN_60, 1800);
        initMARL(CandleType.DAY, 3600);
        return this;
    }
}
