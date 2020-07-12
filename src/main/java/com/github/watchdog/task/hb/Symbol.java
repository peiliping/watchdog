package com.github.watchdog.task.hb;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.hubble.rule.RulesManager;
import com.github.watchdog.task.hb.dataobject.CandleType;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;


public class Symbol {


    private String marketName;

    private String name;

    private Map<CandleType, Series<CandleET>> candleETSeries = Maps.newHashMap();

    private RulesManager rulesManager = new RulesManager();


    public Symbol(String marketName, String name) {

        this.marketName = marketName;
        this.name = name;
    }


    public void initCandleETSeries(CandleType candleType, List<CandleET> candleETList) {

        Series<CandleET> series = this.candleETSeries.get(candleType);
        boolean first = series == null;

        if (first) {
            series = new Series<>(candleType.name(), 128, candleType.interval);
            this.candleETSeries.put(candleType, series);
            //TODO init indicator and rule
        }

        for (CandleET candleET : candleETList) {
            if (candleET.getId() >= series.getMaxId()) {
                series.add(candleET);
            }
        }
    }


    public void addCandleET(CandleType candleType, CandleET candleET) {

        Series<CandleET> series = this.candleETSeries.get(candleType);
        if (candleET.getId() >= series.getMaxId()) {
            series.add(candleET);
        }
    }


    public void initRule(CandleType candleType) {

        //        CandleShockRule candleShockRule = new CandleShockRule(this.name + ".ShockRule", this.candleETSeries, shockRatio, 5);
        //        this.rulesManager.addRule(new PeriodRule(new OverTurnRule(candleShockRule, false), TimeUnit.MINUTES.toSeconds(10)));
    }
}
