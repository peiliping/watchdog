package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.CandleET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.DeltaIS;
import com.github.hubble.indicator.general.PolarIS;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.indicator.specific.BollingPIS;
import com.github.hubble.indicator.specific.RSIPIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.signal.Signal;
import com.github.hubble.signal.SignalRuleResult;
import com.github.watchdog.stream.MsgChannel;
import com.github.watchdog.task.hb.Candles;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BTC_UP extends AbstractHubbleWithCommonRL {


    public BTC_UP(String market, String name, String path) {

        super(market, name, Candles.candles);
        initPositionManager(new PositionManager_UP(path));
    }


    @Override public AbstractHubble init() {

        initShockRL(CandleType.MIN_1, 5, 1d);
        {
            CandleType candleType = CandleType.MIN_15;
            CandleSeries candleSeries = super.candleSeriesManager.getCandleSeries(candleType);
            PolarIS polarIS = IndicatorHelper.create_POLAR_IS(candleSeries, 1);
            BollingPIS bollingPIS = IndicatorHelper.create_Bolling_PIS(candleSeries, 20);

            IRule bollingDownSupport = new BollingDownSupportPSR(buildName(candleType, "Bolling_Down_Support"), polarIS, bollingPIS).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(bollingDownSupport, new SignalRuleResult("Bolling下轨支撑", Signal.BLIND, this)));

            RSIPIS rsiPIS = IndicatorHelper.create_RSI_PIS(candleSeries, 10);
            ToNumIS<CandleET> lowIS = IndicatorHelper.create_LOWEST_IS(candleSeries);
            DeltaIS deltaIS = IndicatorHelper.create_Delta_IS(lowIS);
            IRule newLowest = new ThresholdSRL(buildName(candleType, "New_Lowest"), deltaIS, -30, NumCompareFunction.LTE);
            IRule rsiOverFalling = new ThresholdSRL(buildName(candleType, "RSI_OverFalling"), rsiPIS, 28, NumCompareFunction.LTE).and(newLowest).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(rsiOverFalling, new SignalRuleResult("RSI下跌过度", Signal.CALL, this)));

            IRule rsiMoreRising = new ThresholdSRL(buildName(candleType, "RSI_MoreRising"), rsiPIS, 72, NumCompareFunction.GTE).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(rsiMoreRising, new SignalRuleResult("RSI拉升较多", Signal.FOLD, this)));
        }
        return this;
    }


    @Override public void spark(CandleType candleType, Signal signal, String message) {

        double currentPrice = super.candleSeriesManager.getCandleSeries(candleType).getLast().getClose();
        super.positionManager.handleSignal(signal, currentPrice);
        MsgChannel.getInstance().addResult(String.format("%s.%s(%s %s) %s", super.market, super.name, currentPrice, signal, message));
    }
}
