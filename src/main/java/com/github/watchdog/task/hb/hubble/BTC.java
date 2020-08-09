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
import com.github.hubble.position.PositionManager;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.signal.Signal;
import com.github.hubble.signal.SignalRuleResult;
import com.github.hubble.trend.TrendBGRule;
import com.github.hubble.trend.TrendEntity;
import com.github.hubble.trend.constants.Period;
import com.github.hubble.trend.constants.TrendDegree;
import com.github.hubble.trend.constants.TrendType;
import com.github.watchdog.stream.MsgChannel;
import com.github.watchdog.task.hb.Candles;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BTC extends AbstractHubbleWithCommonRL {


    public BTC(String market, String name, String path) {

        super(market, name, Candles.candles);
        super.positionManager = new PositionManager(path);
        super.positionManager.recoveryState();
    }


    @Override public AbstractHubble init() {

        bindPositionManager(CandleType.MIN_1);
        initShockRL(CandleType.MIN_1, 5, 1d);
        initShortTermRules(CandleType.MIN_30);
        //initMediumTermRules(CandleType.MIN_60);
        //initLongTermRules(CandleType.HOUR_4);
        {
            CandleType candleType = CandleType.MIN_15;
            CandleSeries candleSeries = super.candleSeriesManager.getCandleSeries(candleType);
            PolarIS polarIS = IndicatorHelper.create_POLAR_IS(candleSeries, 1);
            BollingPIS bollingPIS = IndicatorHelper.create_Bolling_PIS(candleSeries, 20);
            IRule bollingDownSupport = new BollingDownSupportPSR(buildName(candleType, "Bolling_Down_Support"), polarIS, bollingPIS).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(bollingDownSupport, new SignalRuleResult("Bolling下轨支撑", Signal.BLIND, this)));

            TrendEntity trendEntity = super.trendManager.get(Period.SHORT);
            IRule mTE = new TrendBGRule("MTE_NOT_TOO_BAD", trendEntity, entity -> TrendType.DOWNWARD != entity.getTrendType() || TrendDegree.POSITIVE == entity.getTrendDegree());
            IRule bollingMidSupport = new BollingMidSupportPSR(buildName(candleType, "Bolling_Middle_Support"), polarIS, bollingPIS).and(mTE).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(bollingMidSupport, new SignalRuleResult("Bolling中轨支撑", Signal.CALL, this)));

            RSIPIS rsiPIS = IndicatorHelper.create_RSI_PIS(candleSeries, 10);

            ToNumIS<CandleET> lowIS = IndicatorHelper.create_LOWEST_IS(candleSeries);
            DeltaIS deltaIS = IndicatorHelper.create_Delta_IS(lowIS);
            IRule newLowest = new ThresholdSRL(buildName(candleType, "New_Lowest"), deltaIS, -50, NumCompareFunction.LTE);
            IRule rsiOverFalling = new ThresholdSRL(buildName(candleType, "RSI_OverFalling"), rsiPIS, 25, NumCompareFunction.LTE).and(newLowest).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(rsiOverFalling, new SignalRuleResult("RSI下跌过度", Signal.CALL, this)));

            IRule rsiMoreRising = new ThresholdSRL(buildName(candleType, "RSI_MoreRising"), rsiPIS, 75, NumCompareFunction.GTE).overTurn(true).period();
            IRule rsiOverRising = new ThresholdSRL(buildName(candleType, "RSI_OverRising"), rsiPIS, 85, NumCompareFunction.GTE).overTurn(true).period();
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

        double currentPrice = super.candleSeriesManager.getCandleSeries(candleType).getLast().getClose();
        super.positionManager.handleSignal(signal, currentPrice);
        String msg = String.format("%s.%s(%s %s) %s", super.market, super.name, currentPrice, signal, message);
        MsgChannel.getInstance().addResult(msg);
    }
}
