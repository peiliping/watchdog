package com.github.watchdog.task.hb;


import com.github.hubble.series.CandleSeries;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesParams;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.function.PISFuncs;
import com.github.hubble.indicator.function.RSVFunction;
import com.github.hubble.indicator.function.WilliamsRFunction;
import com.github.hubble.indicator.general.*;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.RuleResult;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.rule.series.NumberComparePSR;
import com.github.hubble.rule.series.direction.CandleShockSRL;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.watchdog.common.BarkRuleResult;
import com.github.hubble.common.CandleType;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;


public class Symbol {


    private String marketName;

    private String name;

    private double shockRatio;

    private Map<CandleType, CandleSeries> candleETSeries = Maps.newHashMap();

    private Map<CandleType, RulesManager> rulesManagerMap = Maps.newHashMap();


    public Symbol(String marketName, String name, double shockRatio) {

        this.marketName = marketName;
        this.name = name;
        this.shockRatio = shockRatio;
    }


    public void initCandleETSeries(CandleType candleType, List<CandleET> candleETList) {

        SeriesParams base = SeriesParams.builder().size(128).interval(candleType.interval).build();
        CandleSeries series = this.candleETSeries.get(candleType);
        if (series == null) {
            series = new CandleSeries(base.createNew(candleType.name()), this.marketName + "." + this.name);
            this.candleETSeries.put(candleType, series);
            if (CandleType.MIN_1 == candleType) {
                CandleShockSRL candleShockSRL = new CandleShockSRL(buildName("ShockRule"), series, this.shockRatio, 5);
                candleShockSRL.setClazz(BarkRuleResult.class);
                addRule(candleType, candleShockSRL.overTurn(false).period(600), null);
            } else {
                ToNumIS<CandleET> closeSeries = new ToNumIS<>(base.createNew("Close"), candleET -> candleET.getClose());
                closeSeries.after(series);

                MAIS ma05 = new MAIS(base.createNew("MA05"), 5);
                ma05.after(closeSeries);
                MAIS ma10 = new MAIS(base.createNew("MA10"), 10);
                ma10.after(closeSeries);
                MAIS ma30 = new MAIS(base.createNew("MA30"), 30);
                ma30.after(closeSeries);

                STDDIS stdd = new STDDIS(base.createNew("STDD"), 20);
                stdd.after(closeSeries);
                MAIS ma20 = new MAIS(base.createNew("MA20"), 20);
                ma20.after(closeSeries);
                BollingPIS bolling = new BollingPIS(base.createNew("Bolling"), 2, stdd, ma20);

                EMAIS ema12 = new EMAIS(base.createNew("EMA12"), 12, 2);
                ema12.after(closeSeries);
                EMAIS ema26 = new EMAIS(base.createNew("EMA26"), 26, 2);
                ema26.after(closeSeries);
                CalculatePIS dif = new CalculatePIS(base.createNew("DIF"), ema12, ema26, PISFuncs.MINUS);
                EMAIS dea = new EMAIS(base.createNew("DEA"), 9, 2);
                dea.after(dif);
                MACDPIS macd = new MACDPIS(base.createNew("MACD"), dif, dea);

                PolarIS polarIS = new PolarIS(base.createNew("POLAR"), 14);
                polarIS.after(series);
                ToNumIS<TernaryNumberET> rsvIS = new ToNumIS<>(base.createNew("RSV"), new RSVFunction());
                rsvIS.after(polarIS);
                MAIS k = new MAIS(base.createNew("K"), 1);
                k.after(rsvIS);
                MAIS d = new MAIS(base.createNew("D"), 3);
                d.after(k);
                KDJIS kdj = new KDJIS(base.createNew("KDJ"), k, d);

                ToNumIS<TernaryNumberET> williamsrIS = new ToNumIS<>(base.createNew("WR"), new WilliamsRFunction());
                williamsrIS.after(polarIS);

                DeltaIS change = new DeltaIS(base.createNew("Change"));
                change.after(closeSeries);
                ToNumIS<NumberET> up = new ToNumIS<>(base.createNew("Change-Up"), numberET -> Math.max(numberET.getData(), 0));
                up.after(change);
                EMAIS emaUP = new EMAIS(base.createNew("EMA-Change-Up"), 14, 1);
                emaUP.after(up);
                ToNumIS<NumberET> total = new ToNumIS<>(base.createNew("Change-Total"), numberET -> Math.abs(numberET.getData()));
                total.after(change);
                EMAIS emaTotal = new EMAIS(base.createNew("EMA-Change-Total"), 14, 1);
                emaTotal.after(total);
                CalculatePIS calculatePIS = new CalculatePIS(base.createNew("RSI"), emaUP, emaTotal, PISFuncs.PERCENT);

                IRule risingRule = new NumberComparePSR(buildName("CSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT)
                        .and(new NumberComparePSR(buildName("CSR_MA10VS30"), ma10, ma30, NumCompareFunction.GT)).overTurn(true);
                IRule fallingRule = new NumberComparePSR(buildName("CSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT)
                        .and(new NumberComparePSR(buildName("CSR_MA30VS10"), ma30, ma10, NumCompareFunction.GT)).overTurn(true);
                addRule(candleType, risingRule.alternateRule(fallingRule), new BarkRuleResult(buildName(" MA趋势走强")));
                addRule(candleType, fallingRule.alternateRule(risingRule), new BarkRuleResult(buildName(" MA趋势走弱")));

                IRule overSellRule = new ThresholdSRL(buildName("TSR_WR_OS"), williamsrIS, 95, NumCompareFunction.GTE).overTurn(true);
                IRule overBuyRule = new ThresholdSRL(buildName("TSR_WR_OB"), williamsrIS, 5, NumCompareFunction.LTE).overTurn(true);
                addRule(candleType, overSellRule.alternateRule(overBuyRule), new BarkRuleResult(buildName(" WR提示超卖")));
                addRule(candleType, overBuyRule.alternateRule(overSellRule), new BarkRuleResult(buildName(" WR提示超买")));
            }
        }
        for (CandleET candleET : candleETList) {
            addCandleET(candleType, candleET, false);
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


    public void addCandleET(CandleType candleType, CandleET candleET, boolean rule) {

        Series<CandleET> series = this.candleETSeries.get(candleType);
        if (candleET.getId() >= series.getMaxId()) {
            series.add(candleET);
        }
        if (rule) {
            RulesManager rm = this.rulesManagerMap.get(candleType);
            rm.traverseRules(candleET.getId());
        }
    }
}
