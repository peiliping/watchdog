package com.github.hubble;


import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.EMAIS;
import com.github.hubble.indicator.general.PolarIS;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.series.DirectionalSRL;
import com.github.hubble.rule.series.pair.NumComparePSR;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.series.SeriesParams;
import com.github.hubble.trend.TrendRule;
import com.github.hubble.trend.TrendRuleResult;
import com.github.hubble.trend.constants.Period;
import com.github.hubble.trend.constants.TrendType;
import com.github.watchdog.common.BarkRuleResult;
import com.github.watchdog.common.Util;
import com.google.common.collect.Maps;

import java.util.Map;


public abstract class AbstractHubbleWithCommonRL extends AbstractHubble {


    protected double shockRatio = 1d;


    public AbstractHubbleWithCommonRL(String market, String name) {

        super(market, name);
    }


    // 最近M分钟震荡达到N%
    protected void initShockRL(int step, double shockRatio) {

        this.shockRatio = shockRatio;
        CandleType candleType = CandleType.MIN_1;
        CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
        PolarIS polarIs = IndicatorHelper.create_POLAR_IS(candleSeries, step);
        SeriesParams params = SeriesParams.builder().name("ShockRate").candleType(candleSeries.getCandleType()).size(candleSeries.getSize()).build();
        ToNumIS<TernaryNumberET> shockIS = new ToNumIS<>(params, value -> Util.formatPercent(value.getFirst() - value.getThird(), value.getThird()));
        shockIS.after(polarIs);
        IRule shockSRL = new ThresholdSRL(buildName(candleType, "ShockSRL"), shockIS, this.shockRatio, NumCompareFunction.GTE).overTurn(true).ttl(600);
        BarkRuleResult result = new BarkRuleResult("%s.%s is shocking (>= %s%%)", super.market, super.name, this.shockRatio);
        super.rulesManager.addRule(candleType, new Affinity(shockSRL, result));
    }


    protected void initShortTermRules(CandleType candleType) {

        CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
        ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(candleSeries);

        EMAIS ma05 = IndicatorHelper.create_EMA_IS(closeSeries, 5);
        EMAIS ma10 = IndicatorHelper.create_EMA_IS(closeSeries, 10);

        IRule risingRule = new NumComparePSR(buildName(candleType, "ST_NCPSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT, 2);
        IRule fallingRule = new NumComparePSR(buildName(candleType, "ST_NCPSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT, 2);
        TrendRule trendRule = new TrendRule(buildName(candleType, "ST_TrendRule"), risingRule, fallingRule);
        super.rulesManager.addRule(candleType, new Affinity(trendRule, new TrendRuleResult(super.trendManager)));

        Map<TrendType, TrendRule> degreeRules = Maps.newHashMap();
        IRule upRule = new DirectionalSRL.UpDSRL(buildName(candleType, "ST_UPDSRL_MA05"), ma05, 3);
        IRule downRule = new DirectionalSRL.DownDSRL(buildName(candleType, "ST_DOWNDSRL_MA05"), ma05, 2);
        TrendRule degreeRule = new TrendRule(buildName(candleType, "ST_DegreeRule"), upRule, downRule);
        degreeRules.put(TrendType.UPWARD, degreeRule);
        degreeRules.put(TrendType.SHOCK, degreeRule);
        degreeRules.put(TrendType.DOWNWARD, degreeRule);
        super.rulesManager.addRule(candleType, new Affinity(degreeRule, new TrendRuleResult(super.trendManager)));

        super.trendManager.init(candleType, Period.SHORT, trendRule, degreeRules);
    }


    protected void initMediumTermRules(CandleType candleType) {

        CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
        ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(candleSeries);

        EMAIS ma05 = IndicatorHelper.create_EMA_IS(closeSeries, 5);
        EMAIS ma10 = IndicatorHelper.create_EMA_IS(closeSeries, 10);

        IRule risingRule = new NumComparePSR(buildName(candleType, "MT_NCPSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT, 2);
        IRule fallingRule = new NumComparePSR(buildName(candleType, "MT_NCPSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT, 2);
        TrendRule trendRule = new TrendRule(buildName(candleType, "MT_TrendRule"), risingRule, fallingRule);
        super.rulesManager.addRule(candleType, new Affinity(trendRule, new TrendRuleResult(super.trendManager)));

        Map<TrendType, TrendRule> degreeRules = Maps.newHashMap();
        IRule upRule = new DirectionalSRL.UpDSRL(buildName(candleType, "MT_UPDSRL_MA05"), ma05, 3)
                .and(new DirectionalSRL.UpDSRL(buildName(candleType, "MT_UPDSRL_MA10"), ma10, 3));
        IRule downRule = new DirectionalSRL.DownDSRL(buildName(candleType, "MT_DOWNDSRL_MA05"), ma05, 2);
        TrendRule degreeRule = new TrendRule(buildName(candleType, "MT_DegreeRule"), upRule, downRule);
        degreeRules.put(TrendType.UPWARD, degreeRule);
        degreeRules.put(TrendType.SHOCK, degreeRule);
        degreeRules.put(TrendType.DOWNWARD, degreeRule);
        super.rulesManager.addRule(candleType, new Affinity(degreeRule, new TrendRuleResult(super.trendManager)));

        super.trendManager.init(candleType, Period.MEDIUM, trendRule, degreeRules);
    }


    protected void initLongTermRules(CandleType candleType) {

        CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
        ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(candleSeries);

        EMAIS ma05 = IndicatorHelper.create_EMA_IS(closeSeries, 5);
        EMAIS ma10 = IndicatorHelper.create_EMA_IS(closeSeries, 10);
        EMAIS ma25 = IndicatorHelper.create_EMA_IS(closeSeries, 25);

        IRule risingRule = new NumComparePSR(buildName(candleType, "LT_NCPSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT, 2)
                .and(new NumComparePSR(buildName(candleType, "LT_NCPSR_MA10VS25"), ma10, ma25, NumCompareFunction.GT, 2));
        IRule fallingRule = new NumComparePSR(buildName(candleType, "LT_NCPSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT, 2)
                .and(new NumComparePSR(buildName(candleType, "LT_NCPSR__MA25VS05"), ma25, ma05, NumCompareFunction.GT, 2));
        TrendRule trendRule = new TrendRule(buildName(candleType, "LT_TrendRule"), risingRule, fallingRule);
        super.rulesManager.addRule(candleType, new Affinity(trendRule, new TrendRuleResult(super.trendManager)));

        Map<TrendType, TrendRule> degreeRules = Maps.newHashMap();
        IRule upRule = new DirectionalSRL.UpDSRL(buildName(candleType, "LT_UPDSRL_MA05"), ma05, 3)
                .and(new DirectionalSRL.UpDSRL(buildName(candleType, "LT_UPDSRL_MA10"), ma10, 3))
                .and(new DirectionalSRL.UpDSRL(buildName(candleType, "LT_UPDSRL_MA25"), ma25, 3));
        IRule downRule = new DirectionalSRL.DownDSRL(buildName(candleType, "LT_DOWNDSRL_MA05"), ma05, 2);
        TrendRule degreeRule = new TrendRule(buildName(candleType, "LT_DegreeRule"), upRule, downRule);
        degreeRules.put(TrendType.UPWARD, degreeRule);
        degreeRules.put(TrendType.SHOCK, degreeRule);
        degreeRules.put(TrendType.DOWNWARD, degreeRule);
        super.rulesManager.addRule(candleType, new Affinity(degreeRule, new TrendRuleResult(super.trendManager)));

        super.trendManager.init(candleType, Period.LONG, trendRule, degreeRules);
    }
}
