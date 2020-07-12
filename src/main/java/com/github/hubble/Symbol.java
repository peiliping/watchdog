package com.github.hubble;


import com.github.hubble.ele.CandleET;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.rule.condition.OverTurnRule;
import com.github.hubble.rule.condition.PeriodRule;
import com.github.hubble.rule.series.CandleShockRule;
import lombok.Getter;

import java.util.concurrent.TimeUnit;


@Getter
public class Symbol {


    private String name;

    private long interval;

    private Series<CandleET> candleETSeries;

    private RulesManager rulesManager;


    public Symbol(String name, long interval) {

        this.name = name;
        this.interval = interval;
        this.candleETSeries = new Series<>(this.name + "_candles", 128, interval);
        this.rulesManager = new RulesManager();
    }


    public void initIndicators() {

    }


    public void initRule() {

        CandleShockRule candleShockRule = new CandleShockRule(this.name + "_ShockRule", this.candleETSeries, 1d, 5);
        this.rulesManager.addRule(new PeriodRule(new OverTurnRule(candleShockRule, false), TimeUnit.MINUTES.toSeconds(10)));
    }
}
