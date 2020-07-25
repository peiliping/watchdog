package com.github.watchdog.task.hb;


import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.*;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.RuleResult;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.rule.series.NumberComparePSR;
import com.github.hubble.rule.series.direction.CandleShockSRL;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.series.CandleSeriesManager;
import com.github.hubble.series.SeriesParams;
import com.github.watchdog.common.BarkRuleResult;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;


public class Symbol {


    private String marketName;

    private String name;

    private double shockRatio;

    private CandleSeriesManager basicData;

    private Map<CandleType, RulesManager> rulesManagerMap = Maps.newHashMap();


    public Symbol(String marketName, String name, double shockRatio) {

        this.marketName = marketName;
        this.name = name;
        this.shockRatio = shockRatio;
        this.basicData = new CandleSeriesManager(marketName, name, 128);
    }


    public void initCandleETSeries(CandleType candleType, List<CandleET> candleETList) {

        CandleSeries series = this.basicData.getOrCreateCandleSeries(candleType);
        if (CandleType.MIN_1 == candleType) {
            CandleShockSRL candleShockSRL = new CandleShockSRL(buildName("ShockRule"), series, this.shockRatio, 5);
            candleShockSRL.setClazz(BarkRuleResult.class);
            addRule(candleType, candleShockSRL.overTurn(false).period(600), null);
        } else {
            ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(series);
            PolarIS polarIS = IndicatorHelper.create_POLAR_IS(series, 14);

            MAIS ma05 = IndicatorHelper.create_MA_IS(closeSeries, 5);
            MAIS ma10 = IndicatorHelper.create_MA_IS(closeSeries, 10);
            MAIS ma30 = IndicatorHelper.create_MA_IS(closeSeries, 30);

            BollingPIS bolling = IndicatorHelper.create_Bolling_PIS(closeSeries);
            MACDPIS macd = IndicatorHelper.create_MACD_PIS(closeSeries);
            CalculatePIS rsi = IndicatorHelper.create_RSI_PIS(closeSeries);

            KDJIS kdj = IndicatorHelper.create_KDJ_PIS(polarIS);
            ToNumIS<TernaryNumberET> wr = IndicatorHelper.create_WR_IS(polarIS);

            IRule risingRule = new NumberComparePSR(buildName("CSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT)
                    .and(new NumberComparePSR(buildName("CSR_MA10VS30"), ma10, ma30, NumCompareFunction.GT)).overTurn(true);
            IRule fallingRule = new NumberComparePSR(buildName("CSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT)
                    .and(new NumberComparePSR(buildName("CSR_MA30VS10"), ma30, ma10, NumCompareFunction.GT)).overTurn(true);
            addRule(candleType, risingRule.alternateRule(fallingRule), new BarkRuleResult(buildName(" MA趋势走强")));
            addRule(candleType, fallingRule.alternateRule(risingRule), new BarkRuleResult(buildName(" MA趋势走弱")));

            IRule overSellRule = new ThresholdSRL(buildName("TSR_WR_OS"), wr, 95, NumCompareFunction.GTE).overTurn(true);
            IRule overBuyRule = new ThresholdSRL(buildName("TSR_WR_OB"), wr, 5, NumCompareFunction.LTE).overTurn(true);
            addRule(candleType, overSellRule.alternateRule(overBuyRule), new BarkRuleResult(buildName(" WR提示超卖")));
            addRule(candleType, overBuyRule.alternateRule(overSellRule), new BarkRuleResult(buildName(" WR提示超买")));
        }
        this.basicData.addCandleETList(candleType, candleETList);
    }


    public void addCandleET(CandleType candleType, CandleET candleET, boolean rule) {

        this.basicData.addCandleET(candleType, candleET);
        if (rule) {
            RulesManager rm = this.rulesManagerMap.get(candleType);
            rm.traverseRules(candleET.getId());
        }
    }


    private String buildName(String k) {

        return StringUtils.joinWith(".", this.marketName, this.name, k);
    }


    private void addRule(CandleType candleType, IRule rule, RuleResult result) {

        RulesManager rm = this.rulesManagerMap.get(candleType);
        if (rm == null) {
            rm = new RulesManager();
            this.rulesManagerMap.put(candleType, rm);
        }
        rm.addRule(rule, result);
    }
}
