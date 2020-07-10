package com.github.hubble;


import com.github.hubble.ele.CandleET;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.rule.condition.OverTurnRule;
import com.github.hubble.rule.series.ShockSeriesRule;

import java.util.concurrent.TimeUnit;


public class T {


    public static void main(String[] args) {

        RulesManager rulesManager = new RulesManager();

        Series<CandleET> candleETSeries = new Series<>("A", 1024, TimeUnit.MINUTES.toSeconds(1));
        ShockSeriesRule shockSeriesRule = new ShockSeriesRule("A_Candle_Shock", candleETSeries, 2, 5);
        OverTurnRule overTurnRule = new OverTurnRule("", shockSeriesRule);
        rulesManager.addRule(overTurnRule);

        for (int i = 0; i < 100; i++) {
            CandleET candleET = new CandleET(i * 60);
            candleET.setClose(i * 1.5d);
            candleETSeries.add(candleET);
            rulesManager.traverseRules();
        }
    }
}
