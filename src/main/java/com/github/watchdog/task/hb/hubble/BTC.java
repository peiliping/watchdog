package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.AbstractHubbleWithCommonRL;
import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.function.PISFuncs;
import com.github.hubble.indicator.general.CalculatePIS;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.indicator.specific.BollingPIS;
import com.github.hubble.indicator.specific.WRIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.series.SeriesParams;
import com.github.hubble.signal.Signal;
import com.github.hubble.signal.SignalRuleResult;
import com.github.hubble.trend.TrendEntity;
import com.github.hubble.trend.constants.Period;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BTC extends AbstractHubbleWithCommonRL {


    public BTC(String market, String name) {

        super(market, name);
        super.shockRatio = 1d;
    }


    @Override public AbstractHubble init() {

        initShockRL();
        initShortTermRules(CandleType.MIN_5);
        initMediumTermRules(CandleType.MIN_60);
        initLongTermRules(CandleType.HOUR_4);

        {
            CandleType candleType = CandleType.MIN_60;
            CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);

            WRIS wr = IndicatorHelper.create_WR_IS(candleSeries, 14);
            IRule overSellRule = new ThresholdSRL(buildName(candleType, "TSRL_WR_OverSell"), wr, 95d, NumCompareFunction.GTE).overTurn(true).period(120);
            IRule overBuyRule = new ThresholdSRL(buildName(candleType, "TSRL_WR_OverBuy"), wr, 5d, NumCompareFunction.LTE).overTurn(true).period(120);
            super.rulesManager.addRule(candleType, new Affinity(overSellRule, new SignalRuleResult("超卖", Signal.INPUT, this)));
            super.rulesManager.addRule(candleType, new Affinity(overBuyRule, new SignalRuleResult("超买", Signal.OUTPUT, this)));


            BollingPIS bollingPIS = IndicatorHelper.create_Bolling_PIS(candleSeries, 20);
            SeriesParams base = SeriesParams.builder().candleType(candleType).size(bollingPIS.getSize()).build();

            ToNumIS<TernaryNumberET> bollingUp = IndicatorHelper.create_Bolling_Up_IS(bollingPIS);
            ToNumIS<CandleET> highSeries = IndicatorHelper.create_HIGH_IS(candleSeries);
            CalculatePIS overFlow = new CalculatePIS(base.createNew("OverFlow"), highSeries, bollingUp, PISFuncs.OVERPERCENT);
            IRule upPressure = new ThresholdSRL(buildName(candleType, "Bolling_Up_Pressure"), overFlow, 1d, NumCompareFunction.GTE).overTurn(true).period(120);
            super.rulesManager.addRule(candleType, new Affinity(upPressure, new SignalRuleResult("Bolling上轨压迫", Signal.OUTPUT, this)));

            ToNumIS<TernaryNumberET> bollingDown = IndicatorHelper.create_Bolling_Down_IS(bollingPIS);
            ToNumIS<CandleET> lowSeries = IndicatorHelper.create_LOW_IS(candleSeries);
            CalculatePIS dive = new CalculatePIS(base.createNew("Dive"), lowSeries, bollingDown, PISFuncs.OVERPERCENT);
            IRule downSupport = new ThresholdSRL(buildName(candleType, "Bolling_Down_Support"), dive, -0.2d, NumCompareFunction.LTE).overTurn(true).period(120);
            super.rulesManager.addRule(candleType, new Affinity(downSupport, new SignalRuleResult("Bolling下轨支撑", Signal.INPUT, this)));
        }
        return this;
    }


    @Override public void spark(CandleType candleType, Signal signal) {

        double currentPrice = super.candleSeriesManager.getOrCreateCandleSeries(candleType).getLast().getClose();
        TrendEntity st = super.trendManager.get(Period.SHORT);
        TrendEntity mt = super.trendManager.get(Period.MEDIUM);
        TrendEntity lt = super.trendManager.get(Period.LONG);
        log.warn("{}.{} Spark : {} {} , {} {} {}", super.market, super.name, currentPrice, signal, st.toString(), mt.toString(), lt.toString());
    }
}
