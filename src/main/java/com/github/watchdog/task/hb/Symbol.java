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
            series = new Series<>(buildName(candleType.name()), 128, candleType.interval);
            this.candleETSeries.put(candleType, series);
            if (CandleType.MIN_1 == candleType) {
                CandleShockRule candleShockRule = new CandleShockRule(buildName("ShockRule"), series, this.shockRatio, 5);
                candleShockRule.setClazz(BarkRuleResult.class);
                addRule(candleType, candleShockRule.overTurn(false).period(600), null);
            } else {
                long duration = candleType.interval;
                MAIndicatorSeries ma05 = new MAIndicatorSeries(buildName("MA05"), 128, duration, 5);
                MAIndicatorSeries ma10 = new MAIndicatorSeries(buildName("MA10"), 128, duration, 10);
                MAIndicatorSeries ma30 = new MAIndicatorSeries(buildName("MA30"), 128, duration, 30);

                CustomCompare<NumberET> cc = (e1, e2) -> Double.compare(e1.getData(), e2.getData());
                CompareIndicatorSeries<CandleET, NumberET> ma05VS10 = new CompareIndicatorSeries<>(buildName("CIS_MA05VS10"), 128, duration, ma05, ma10, cc);
                CompareIndicatorSeries<CandleET, NumberET> ma05VS30 = new CompareIndicatorSeries<>(buildName("CIS_MA05VS30"), 128, duration, ma05, ma30, cc);
                CompareIndicatorSeries<CandleET, NumberET> ma30VS05 = new CompareIndicatorSeries<>(buildName("CIS_MA30VS05"), 128, duration, ma30, ma05, cc);
                series.bind(ma05VS10, ma05VS30, ma30VS05);

                {
                    IRule enterRule = new BooleanRule(buildName("BR_MA05VS10"), ma05VS10).and(new BooleanRule(buildName("BR_MA10VS30"), ma05VS30)).overTurn(true);
                    String msg = buildName(" MA趋势走强");
                    addRule(candleType, enterRule, new BarkRuleResult(msg));
                }
                {
                    IRule exitRule = new BooleanRule(buildName("BR_MA30VS05"), ma30VS05).overTurn(true);
                    String msg = buildName(" MA趋势走弱");
                    addRule(candleType, exitRule, new BarkRuleResult(msg));
                }

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
