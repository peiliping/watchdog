package com.github.watchdog.task.hb;


import com.github.hubble.AbstractHubble;
import com.github.hubble.common.CandleType;
import com.github.hubble.series.CandleSeriesManager;
import com.github.watchdog.task.hb.hubble.BTC;


public class BackTest {


    public static void main(String[] args) {

        AbstractHubble btc = new BTC("Huobi", "btcusdt", "/tmp/state");
        CandleSeriesManager csm = btc.getCandleSeriesManager();
        for (CandleType candleType : Candles.candles) {
            if (CandleType.MIN_1 != candleType) {
                csm.candleSeriesBridge(CandleType.MIN_1, candleType);
            }
        }
        //load 1min candle for background

        //load 1min candle for backtest

    }

}
