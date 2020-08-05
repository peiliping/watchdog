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
import com.github.hubble.trend.TrendEntity;
import com.github.hubble.trend.constants.Period;
import com.github.watchdog.stream.MsgChannel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BTC extends AbstractHubbleWithCommonRL {


    public BTC(String market, String name) {

        super(market, name);
        super.shockRatio = 1d;
        super.positionManager = new PositionManager();
    }


    @Override public AbstractHubble init() {

        initShockRL();
        initShortTermRules(CandleType.MIN_5);
        initMediumTermRules(CandleType.MIN_60);
        initLongTermRules(CandleType.HOUR_4);

        {
            CandleType candleType = CandleType.MIN_15;
            CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
            PolarIS polarIS = IndicatorHelper.create_POLAR_IS(candleSeries, 1);

            BollingPIS bollingPIS = IndicatorHelper.create_Bolling_PIS(candleSeries, 20);
            IRule bollingDownSupport = new BollingDownSupportPSR(buildName(candleType, "Bolling_Down_Support"), polarIS, bollingPIS).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(bollingDownSupport, new SignalRuleResult("Bolling下轨支撑", Signal.BLIND, this)));

            RSIPIS rsiPIS = IndicatorHelper.create_RSI_PIS(candleSeries, 10);
            IRule rsiOverRising = new ThresholdSRL(buildName(candleType, "RSI_OverRising"), rsiPIS, 80, NumCompareFunction.GT).overTurn(true).period();
            IRule rsiOverFalling = new ThresholdSRL(buildName(candleType, "RSI_OverFalling"), rsiPIS, 20, NumCompareFunction.LT).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(rsiOverRising, new SignalRuleResult("RSI过度拉升", Signal.FOLD, this)));
            super.rulesManager.addRule(candleType, new Affinity(rsiOverFalling, new SignalRuleResult("RSI过度下跌", Signal.CALL, this)));
        }
        {
            CandleType candleType = CandleType.MIN_30;
            CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
            PolarIS polarIS = IndicatorHelper.create_POLAR_IS(candleSeries, 1);
            BollingPIS bollingPIS = IndicatorHelper.create_Bolling_PIS(candleSeries, 20);

            IRule bollingMidRising = new BollingMidRisingPSR(buildName(candleType, "Bolling_Middle_Rising"), polarIS, bollingPIS).overTurn(true).period();
            IRule bollingMidSupport = new BollingMidSupportPSR(buildName(candleType, "Bolling_Middle_Support"), polarIS, bollingPIS).overTurn(true).period();
            IRule bollingMidPressure = new BollingMidPressurePSR(buildName(candleType, "Bolling_Middle_Pressure"), polarIS, bollingPIS).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(bollingMidRising, new SignalRuleResult("Bolling中轨突破", Signal.CALL, this)));
            super.rulesManager.addRule(candleType, new Affinity(bollingMidSupport, new SignalRuleResult("Bolling中轨支撑", Signal.CALL, this)));
            super.rulesManager.addRule(candleType, new Affinity(bollingMidPressure, new SignalRuleResult("Bolling中轨压迫", Signal.FOLD, this)));

            RSIPIS rsiPIS = IndicatorHelper.create_RSI_PIS(candleSeries, 10);
            IRule rsiOverRising = new ThresholdSRL(buildName(candleType, "RSI_OverRising"), rsiPIS, 75, NumCompareFunction.GT).overTurn(true).period();
            IRule rsiOverFalling = new ThresholdSRL(buildName(candleType, "RSI_OverFalling"), rsiPIS, 25, NumCompareFunction.LT).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(rsiOverRising, new SignalRuleResult("RSI过度拉升", Signal.FOLD, this)));
            super.rulesManager.addRule(candleType, new Affinity(rsiOverFalling, new SignalRuleResult("RSI过度下跌", Signal.CALL, this)));
        }
        return this;
    }


    @Override public void spark(CandleType candleType, Signal signal, String message) {

        double currentPrice = super.candleSeriesManager.getOrCreateCandleSeries(candleType).getLast().getClose();
        TrendEntity st = super.trendManager.get(Period.SHORT);
        TrendEntity mt = super.trendManager.get(Period.MEDIUM);
        TrendEntity lt = super.trendManager.get(Period.LONG);
        String msg = String.format("%s.%s(%s %s) %s, %s %s %s", super.market, super.name, currentPrice, signal, message, st.toString(), mt.toString(), lt.toString());
        MsgChannel.getInstance().addResult(msg);
        super.positionManager.handleSignal(signal, currentPrice);
    }
}
