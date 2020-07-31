package com.github.hubble;


import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.MAIS;
import com.github.hubble.indicator.general.PolarIS;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.trend.TrendRule;
import com.github.hubble.trend.TrendRuleResult;
import com.github.hubble.rule.series.DirectionalSRL;
import com.github.hubble.rule.series.pair.NumComparePSR;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.series.SeriesParams;
import com.github.hubble.trend.constants.Period;
import com.github.hubble.trend.constants.TrendType;
import com.github.watchdog.common.BarkRuleResult;
import com.google.common.collect.Maps;

import java.util.Map;


public abstract class AbstractHubbleWithCommonRL extends AbstractHubble {


    protected double shockRatio;


    public AbstractHubbleWithCommonRL(String market, String name) {

        super(market, name);
        this.shockRatio = 1d;
    }


    // 最近M分钟震荡达到N%
    protected void initShockRL() {

        CandleSeries min1_CS = super.candleSeriesManager.getOrCreateCandleSeries(CandleType.MIN_1);
        PolarIS min1_PolarIS = IndicatorHelper.create_POLAR_IS(min1_CS, 5);
        SeriesParams params = SeriesParams.builder().name("ShockRate").candleType(min1_CS.getCandleType()).size(min1_CS.getSize()).build();
        ToNumIS<TernaryNumberET> shockIS = new ToNumIS<>(params, value -> (double) Math.round((value.getFirst() - value.getThird()) / value.getThird() * 10000) / 100);
        shockIS.after(min1_PolarIS);
        IRule shockSRL = new ThresholdSRL(buildName(CandleType.MIN_1, "ShockSRL"), shockIS, this.shockRatio, NumCompareFunction.GTE).overTurn(true).period(900);
        BarkRuleResult result = new BarkRuleResult("%s.%s is shocking (>= %s%%)", super.market, super.name, this.shockRatio);
        super.rulesManager.addRule(CandleType.MIN_1, new Affinity(shockSRL, result));
    }


    protected void initShortTermRules(CandleType candleType) {

        CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
        ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(candleSeries);

        MAIS ma05 = IndicatorHelper.create_MA_IS(closeSeries, 5);
        MAIS ma10 = IndicatorHelper.create_MA_IS(closeSeries, 10);

        IRule risingRule = new NumComparePSR(buildName(candleType, "ST_NCPSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT, 2);
        IRule fallingRule = new NumComparePSR(buildName(candleType, "ST_NCPSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT, 2);
        TrendRule trendRule = new TrendRule("ST_TrendRule", risingRule, fallingRule);
        super.rulesManager.addRule(candleType, new Affinity(trendRule, new TrendRuleResult(super.trendManager, candleType)));

        Map<TrendType, TrendRule> degreeRules = Maps.newHashMap();
        IRule upRule = new DirectionalSRL.UpDSRL("MT_UPDSRL_MA05", ma05, 3);
        IRule downRule = new DirectionalSRL.DownDSRL("MT_DOWNDSRL_MA05", ma05, 2);
        TrendRule degreeRule = new TrendRule("MT_DegreeRule", upRule, downRule);
        degreeRules.put(TrendType.UPWARD, degreeRule);
        degreeRules.put(TrendType.SHOCK, degreeRule);
        degreeRules.put(TrendType.DOWNWARD, degreeRule);
        super.rulesManager.addRule(candleType, new Affinity(degreeRule, new TrendRuleResult(super.trendManager, candleType)));

        super.trendManager.init(candleType, Period.SHORT, trendRule, degreeRules);
    }


    protected void initMediumTermRules(CandleType candleType) {

        CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
        ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(candleSeries);

        MAIS ma05 = IndicatorHelper.create_MA_IS(closeSeries, 5);
        MAIS ma10 = IndicatorHelper.create_MA_IS(closeSeries, 10);

        IRule risingRule = new NumComparePSR(buildName(candleType, "MT_NCPSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT, 2);
        IRule fallingRule = new NumComparePSR(buildName(candleType, "MT_NCPSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT, 2);
        TrendRule trendRule = new TrendRule("MT_TrendRule", risingRule, fallingRule);
        super.rulesManager.addRule(candleType, new Affinity(trendRule, new TrendRuleResult(super.trendManager, candleType)));

        Map<TrendType, TrendRule> degreeRules = Maps.newHashMap();
        IRule upRule = new DirectionalSRL.UpDSRL("MT_UPDSRL_MA05", ma05, 3).and(new DirectionalSRL.UpDSRL("MT_UPDSRL_MA10", ma10, 3));
        IRule downRule = new DirectionalSRL.DownDSRL("MT_DOWNDSRL_MA05", ma05, 2);
        TrendRule degreeRule = new TrendRule("MT_DegreeRule", upRule, downRule);
        degreeRules.put(TrendType.UPWARD, degreeRule);
        degreeRules.put(TrendType.SHOCK, degreeRule);
        degreeRules.put(TrendType.DOWNWARD, degreeRule);
        super.rulesManager.addRule(candleType, new Affinity(degreeRule, new TrendRuleResult(super.trendManager, candleType)));

        super.trendManager.init(candleType, Period.MEDIUM, trendRule, degreeRules);
    }


    protected void initLongTermRules(CandleType candleType) {

        CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
        ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(candleSeries);

        MAIS ma05 = IndicatorHelper.create_MA_IS(closeSeries, 5);
        MAIS ma10 = IndicatorHelper.create_MA_IS(closeSeries, 10);
        MAIS ma25 = IndicatorHelper.create_MA_IS(closeSeries, 25);

        IRule risingRule = new NumComparePSR(buildName(candleType, "LT_NCPSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT, 2)
                .and(new NumComparePSR(buildName(candleType, "LT_NCPSR_MA10VS25"), ma10, ma25, NumCompareFunction.GT, 2));
        IRule fallingRule = new NumComparePSR(buildName(candleType, "LT_NCPSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT, 2)
                .and(new NumComparePSR(buildName(candleType, "LT_NCPSR__MA25VS05"), ma25, ma05, NumCompareFunction.GT, 2));
        TrendRule trendRule = new TrendRule("LT_TrendRule", risingRule, fallingRule);
        super.rulesManager.addRule(candleType, new Affinity(trendRule, new TrendRuleResult(super.trendManager, candleType)));

        Map<TrendType, TrendRule> degreeRules = Maps.newHashMap();
        IRule upRule = new DirectionalSRL.UpDSRL("LT_UPDSRL_MA05", ma05, 3)
                .and(new DirectionalSRL.UpDSRL("LT_UPDSRL_MA10", ma10, 3))
                .and(new DirectionalSRL.UpDSRL("LT_UPDSRL_MA25", ma25, 3));
        IRule downRule = new DirectionalSRL.DownDSRL("LT_DOWNDSRL_MA05", ma05, 2);
        TrendRule degreeRule = new TrendRule("LT_DegreeRule", upRule, downRule);
        degreeRules.put(TrendType.UPWARD, degreeRule);
        degreeRules.put(TrendType.SHOCK, degreeRule);
        degreeRules.put(TrendType.DOWNWARD, degreeRule);
        super.rulesManager.addRule(candleType, new Affinity(degreeRule, new TrendRuleResult(super.trendManager, candleType)));

        super.trendManager.init(candleType, Period.LONG, trendRule, degreeRules);
    }
}
