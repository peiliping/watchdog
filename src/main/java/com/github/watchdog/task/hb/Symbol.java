package com.github.watchdog.task.hb;


import com.github.hubble.RuleResult;
import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.CustomCompare;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.MAIndicatorSeries;
import com.github.hubble.indicator.group.CompareIndicatorSeries;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.rule.series.BooleanRule;
import com.github.hubble.rule.series.CandleShockRule;
import com.github.watchdog.task.hb.dataobject.CandleType;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;


public class Symbol {


    private String marketName;

    private String name;

    private double shockRatio;

    private Map<CandleType, Series<CandleET>> candleETSeries = Maps.newHashMap();

    private Map<CandleType, RulesManager> rulesManagerMap = Maps.newHashMap();


    public Symbol(String marketName, String name, double shockRatio) {

        this.marketName = marketName;
        this.name = name;
        this.shockRatio = shockRatio;
    }


    public void initCandleETSeries(CandleType candleType, List<CandleET> candleETList) {

        Series<CandleET> series = this.candleETSeries.get(candleType);
        boolean first = series == null;

        if (first) {
            series = new Series<>(candleType.name(), 128, candleType.interval);
            this.candleETSeries.put(candleType, series);
            if (CandleType.MIN_1 == candleType) {
                String ruleName = StringUtils.joinWith(".", this.marketName, this.name, "ShockRule");
                addRule(candleType, new CandleShockRule(ruleName, series, this.shockRatio, 5).overTurn(false).period(600), null);
            } else {
                long duration = candleType.interval;
                MAIndicatorSeries ma05 = new MAIndicatorSeries("MA05", 128, duration, 5);
                MAIndicatorSeries ma10 = new MAIndicatorSeries("MA10", 128, duration, 10);
                MAIndicatorSeries ma30 = new MAIndicatorSeries("MA30", 128, duration, 30);

                CustomCompare<NumberET> cc = (e1, e2) -> Double.compare(e1.getData(), e2.getData());
                CompareIndicatorSeries<CandleET, NumberET> ma05VS10 = new CompareIndicatorSeries<>("A_ma05VS10", 128, duration, ma05, ma10, cc);
                CompareIndicatorSeries<CandleET, NumberET> ma05VS30 = new CompareIndicatorSeries<>("A_ma05VS30", 128, duration, ma05, ma30, cc);
                CompareIndicatorSeries<CandleET, NumberET> ma30VS05 = new CompareIndicatorSeries<>("A_ma30VS05", 128, duration, ma30, ma05, cc);
                series.bind(ma05VS10, ma05VS30, ma30VS05);

                {
                    IRule enterRule = new BooleanRule("ma05VS10BR", ma05VS10).and(new BooleanRule("ma10VS30BR", ma05VS30)).overTurn(false);
                    String msg = StringUtils.joinWith(".", this.marketName, this.name) + " MA趋势走强 .";
                    addRule(candleType, enterRule, new RuleResult(msg));
                }
                {
                    IRule exitRule = new BooleanRule("ma30VS05BR", ma30VS05).overTurn(false);
                    String msg = StringUtils.joinWith(".", this.marketName, this.name) + " MA趋势走弱 .";
                    addRule(candleType, exitRule, new RuleResult(msg));
                }

            }
        }

        for (CandleET candleET : candleETList) {
            addCandleET(candleType, candleET, false);
        }
    }


    private void addRule(CandleType candleType, IRule rule, RuleResult result) {

        RulesManager rm = getOrCreateRM(candleType);
        rm.addRule(rule, result);
    }


    private RulesManager getOrCreateRM(CandleType candleType) {

        RulesManager rm = this.rulesManagerMap.get(candleType);
        if (rm == null) {
            rm = new RulesManager();
            this.rulesManagerMap.put(candleType, rm);
        }
        return rm;
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
