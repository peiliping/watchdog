package com.github.hubble;


import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.CustomCompare;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.MAIndicatorSeries;
import com.github.hubble.indicator.group.GreaterThanIndicatorSeries;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.rule.condition.OverTurnRule;
import com.github.hubble.rule.logic.AndRule;
import com.github.hubble.rule.series.BooleanRule;

import java.util.concurrent.TimeUnit;


public class T {


    public static void main(String[] args) {

        long interval = TimeUnit.MINUTES.toSeconds(1);

        RulesManager rulesManager = new RulesManager();

        Series<CandleET> candleETSeries = new Series<>("A", 128, interval);

        MAIndicatorSeries ma05 = new MAIndicatorSeries("A_ma05", 128, interval, 5);
        MAIndicatorSeries ma10 = new MAIndicatorSeries("A_ma10", 128, interval, 10);
        MAIndicatorSeries ma30 = new MAIndicatorSeries("A_ma30", 128, interval, 30);

        CustomCompare<NumberET> customCompare = (e1, e2) -> Double.compare(e1.getData(), e2.getData());

        GreaterThanIndicatorSeries<CandleET, NumberET> ma05VS10 =
                new GreaterThanIndicatorSeries<>("A_ma05VS10", 128, interval, ma05, ma10, customCompare);
        GreaterThanIndicatorSeries<CandleET, NumberET> ma05VS30 =
                new GreaterThanIndicatorSeries<>("A_ma05VS30", 128, interval, ma05, ma30, customCompare);

        BooleanRule ma05VS10BR = new BooleanRule("ma05VS10BR", ma05VS10);
        BooleanRule ma05VS30BR = new BooleanRule("ma05VS30BR", ma05VS30);

        AndRule andRule = new AndRule("AndRule", ma05VS10BR, ma05VS30BR);
        OverTurnRule overTurnRule = new OverTurnRule("OverTurnRule", andRule, false);
        rulesManager.addRule(overTurnRule);

        candleETSeries.regist(ma05VS10).regist(ma05VS30);

        for (int i = 0; i < 100; i++) {
            CandleET candleET = new CandleET(i * 60);
            candleET.setClose(i * 1.5d);
            candleETSeries.add(candleET);
            rulesManager.traverseRules();
        }
    }
}
