package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.AbstractHubbleWithCommonRL;
import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.PolarIS;
import com.github.hubble.indicator.specific.BollingPIS;
import com.github.hubble.indicator.specific.RSIPIS;
import com.github.hubble.position.PositionManager;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.signal.Signal;
import com.github.hubble.signal.SignalRuleResult;
import com.github.watchdog.stream.MsgChannel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BTC extends AbstractHubbleWithCommonRL {


    public BTC(String market, String name) {

        super(market, name);
        super.positionManager = new PositionManager();
    }


    @Override public AbstractHubble init() {

        for (CandleType ct : CandleType.values()) {
            super.candleSeriesManager.getOrCreateCandleSeries(ct);
        }

        bindPositionManager(CandleType.MIN_1);
        initShockRL(5, 1d);
        {
            CandleType candleType = CandleType.MIN_15;
            CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
            PolarIS polarIS = IndicatorHelper.create_POLAR_IS(candleSeries, 1);
            BollingPIS bollingPIS = IndicatorHelper.create_Bolling_PIS(candleSeries, 20);
            IRule bollingDownSupport = new BollingDownSupportPSR(buildName(candleType, "Bolling_Down_Support"), polarIS, bollingPIS).overTurn(true).period();
            IRule bollingMidSupport = new BollingMidSupportPSR(buildName(candleType, "Bolling_Middle_Support"), polarIS, bollingPIS).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(bollingDownSupport, new SignalRuleResult("Bolling下轨支撑", Signal.BLIND, this)));
            super.rulesManager.addRule(candleType, new Affinity(bollingMidSupport, new SignalRuleResult("Bolling中轨支撑", Signal.CALL, this)));

            RSIPIS rsiPIS = IndicatorHelper.create_RSI_PIS(candleSeries, 10);
            IRule rsiOverFalling = new ThresholdSRL(buildName(candleType, "RSI_OverFalling"), rsiPIS, 25, NumCompareFunction.LT).overTurn(true).period();
            IRule rsiMoreRising = new ThresholdSRL(buildName(candleType, "RSI_MoreRising"), rsiPIS, 75, NumCompareFunction.GTE).overTurn(true).period();
            IRule rsiOverRising = new ThresholdSRL(buildName(candleType, "RSI_OverRising"), rsiPIS, 80, NumCompareFunction.GTE).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(rsiOverFalling, new SignalRuleResult("RSI下跌过度", Signal.CALL, this)));
            super.rulesManager.addRule(candleType, new Affinity(rsiMoreRising, new SignalRuleResult("RSI拉升较多", Signal.FOLD, this)));
            super.rulesManager.addRule(candleType, new Affinity(rsiOverRising, new SignalRuleResult("RSI拉升过度", Signal.MUCK, this)));
        }
        return this;
    }


    //IRule bollingMidRising = new BollingMidRisingPSR(buildName(candleType, "Bolling_Middle_Rising"), polarIS, bollingPIS).overTurn(true).period();
    //IRule bollingMidPressure = new BollingMidPressurePSR(buildName(candleType, "Bolling_Middle_Pressure"), polarIS, bollingPIS).overTurn(true).period();
    //super.rulesManager.addRule(candleType, new Affinity(bollingMidRising, new SignalRuleResult("Bolling中轨突破", Signal.CALL, this)));
    //super.rulesManager.addRule(candleType, new Affinity(bollingMidPressure, new SignalRuleResult("Bolling中轨压迫", Signal.FOLD, this)));


    @Override public void spark(CandleType candleType, Signal signal, String message) {

        double currentPrice = super.candleSeriesManager.getOrCreateCandleSeries(candleType).getLast().getClose();
        super.positionManager.handleSignal(signal, currentPrice);
        String msg = String.format("%s.%s(%s %s) %s", super.market, super.name, currentPrice, signal, message);
        MsgChannel.getInstance().addResult(msg);
    }
}
