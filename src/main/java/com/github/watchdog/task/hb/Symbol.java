package com.github.watchdog.task.hb;


import com.github.hubble.CandleSeries;
import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.CustomCompare;
import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.function.PISFuncs;
import com.github.hubble.indicator.function.RSVFunction;
import com.github.hubble.indicator.function.WilliamsRFunction;
import com.github.hubble.indicator.general.*;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.RuleResult;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.rule.series.CompareSeriesRule;
import com.github.hubble.rule.series.direction.CandleShockRule;
import com.github.watchdog.common.BarkRuleResult;
import com.github.watchdog.task.hb.dataobject.CandleType;
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

        CandleSeries series = this.candleETSeries.get(candleType);
        if (series == null) {
            series = new CandleSeries(buildName(candleType.name()), 128, candleType.interval);
            this.candleETSeries.put(candleType, series);
            if (CandleType.MIN_1 == candleType) {
                CandleShockRule candleShockRule = new CandleShockRule(buildName("ShockRule"), series, this.shockRatio, 5);
                candleShockRule.setClazz(BarkRuleResult.class);
                addRule(candleType, candleShockRule.overTurn(false).period(600), null);
            } else {
                ToNumIS<CandleET> closeSeries = new ToNumIS<>(buildName("Close"), 128, candleType.interval, candleET -> candleET.getClose());
                series.bind(closeSeries);

                DeltaIS change = new DeltaIS(buildName("Change"), 128, candleType.interval);
                closeSeries.bind(change);
                ToNumIS<NumberET> up = new ToNumIS<>(buildName("Change-Up"), 128, candleType.interval, numberET -> Math.max(numberET.getData(), 0));
                EMAIS emaUP = new EMAIS(buildName("EMA-Change-Up"), 128, candleType.interval, 14, 1);
                up.bind(emaUP);
                ToNumIS<NumberET> total = new ToNumIS<>(buildName("Change-Total"), 128, candleType.interval, numberET -> Math.abs(numberET.getData()));
                EMAIS emaTotal = new EMAIS(buildName("EMA-Change-Total"), 128, candleType.interval, 14, 1);
                total.bind(emaTotal);
                CalculatePIS calculatePIS = new CalculatePIS(buildName("Calculate"), 128, candleType.interval, emaUP, emaTotal, PISFuncs.PERCENT);
                change.bind(up, total, calculatePIS);

                MAIS ma05 = new MAIS(buildName("MA05"), 128, candleType.interval, 5);
                MAIS ma10 = new MAIS(buildName("MA10"), 128, candleType.interval, 10);
                MAIS ma30 = new MAIS(buildName("MA30"), 128, candleType.interval, 30);
                closeSeries.bind(ma05, ma10, ma30);

                EMAIS ema12 = new EMAIS(buildName("EMA12"), 128, candleType.interval, 12, 2);
                EMAIS ema26 = new EMAIS(buildName("EMA26"), 128, candleType.interval, 26, 2);
                CalculatePIS dif = new CalculatePIS(buildName("DIF"), 128, candleType.interval, ema12, ema26, PISFuncs.SUBTRACTION);
                EMAIS dea = new EMAIS(buildName("DEA"), 128, candleType.interval, 9, 2);
                dif.bind(dea);
                MACDPIS macd = new MACDPIS(buildName("MACD"), 128, candleType.interval, dif, dea);
                closeSeries.bind(ema12, ema26, dif, macd);

                STDDIS stdd = new STDDIS(buildName("STDD"), 128, candleType.interval, 20);
                MAIS ma20 = new MAIS(buildName("MA20"), 128, candleType.interval, 20);
                BollingPIS bolling = new BollingPIS(buildName("Bolling"), 128, candleType.interval, 2, stdd, ma20);
                closeSeries.bind(stdd, ma20, bolling);

                PolarIS polarIS = new PolarIS(buildName("POLAR"), 128, candleType.interval, 14);
                series.bind(polarIS);
                ToNumIS<TernaryNumberET> williamsrIS = new ToNumIS<>(buildName("WR"), 128, candleType.interval, new WilliamsRFunction());
                polarIS.bind(williamsrIS);
                ToNumIS<TernaryNumberET> rsvIS = new ToNumIS<>(buildName("RSV"), 128, candleType.interval, new RSVFunction());
                polarIS.bind(rsvIS);
                MAIS k = new MAIS(buildName("K"), 128, candleType.interval, 1);
                rsvIS.bind(k);
                MAIS d = new MAIS(buildName("D"), 128, candleType.interval, 3);
                k.bind(d);
                KDJIS kdj = new KDJIS(buildName("KDJ"), 128, candleType.interval, k, d);
                rsvIS.bind(kdj);

                IRule risingRule = new CompareSeriesRule<>(buildName("CSR_MA05VS10"), ma05, ma10, CustomCompare.numberETCompare)
                        .and(new CompareSeriesRule<>(buildName("CSR_MA10VS30"), ma10, ma30, CustomCompare.numberETCompare)).overTurn(true);
                IRule fallingRule = new CompareSeriesRule<>(buildName("CSR_MA10VS05"), ma10, ma05, CustomCompare.numberETCompare)
                        .and(new CompareSeriesRule<>(buildName("CSR_MA30VS10"), ma30, ma10, CustomCompare.numberETCompare)).overTurn(true);
                addRule(candleType, risingRule.alternateRule(fallingRule), new BarkRuleResult(buildName(" MA趋势走强")));
                addRule(candleType, fallingRule.alternateRule(risingRule), new BarkRuleResult(buildName(" MA趋势走弱")));
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
