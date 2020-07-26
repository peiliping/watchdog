package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.AbstractHubbleWithCommonRL;
import com.github.hubble.common.CandleType;
import com.github.hubble.series.CandleSeries;


public class BTC extends AbstractHubbleWithCommonRL {


    public BTC(String market, String name) {

        super(market, name);
        super.shockRatio = 0.8d;
    }


    @Override public AbstractHubble init() {

        initShockSRL();

        CandleSeries min60_CS = super.candleSeriesManager.getOrCreateCandleSeries(CandleType.MIN_60);
        CandleSeries day_CS = super.candleSeriesManager.getOrCreateCandleSeries(CandleType.DAY);
        return this;
    }
}
