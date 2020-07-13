package com.github.watchdog.task.hb;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.RulesManager;
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
                addRule(candleType, new CandleShockRule(ruleName, series, this.shockRatio, 5).overTurn(false).period(600));
            }
        }

        for (CandleET candleET : candleETList) {
            if (candleET.getId() >= series.getMaxId()) {
                series.add(candleET);
            }
        }
    }


    private void addRule(CandleType candleType, IRule rule) {

        RulesManager rm = getOrCreateRM(candleType);
        rm.addRule(rule);
    }


    private RulesManager getOrCreateRM(CandleType candleType) {

        RulesManager rm = this.rulesManagerMap.get(candleType);
        if (rm == null) {
            rm = new RulesManager();
            this.rulesManagerMap.put(candleType, rm);
        }
        return rm;
    }


    public void addCandleET(CandleType candleType, CandleET candleET) {

        Series<CandleET> series = this.candleETSeries.get(candleType);
        if (candleET.getId() >= series.getMaxId()) {
            series.add(candleET);
        }
        RulesManager rm = this.rulesManagerMap.get(candleType);
        rm.traverseRules(candleET.getId());
    }
}
