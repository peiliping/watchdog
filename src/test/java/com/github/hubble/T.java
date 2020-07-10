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
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
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
        GreaterThanIndicatorSeries<CandleET, NumberET> ma10VS30 =
                new GreaterThanIndicatorSeries<>("A_ma10VS30", 128, interval, ma10, ma30, customCompare);

        candleETSeries.regist(ma05VS10).regist(ma10VS30);

        BooleanRule ma05VS10BR = new BooleanRule("ma05VS10BR", ma05VS10);
        BooleanRule ma10VS30BR = new BooleanRule("ma10VS30BR", ma10VS30);
        AndRule andRule = new AndRule("AndRule", ma05VS10BR, ma10VS30BR);
        OverTurnRule overTurnRule = new OverTurnRule("OverTurnRule", andRule, true);
        overTurnRule.setMsg("买入信号");
        rulesManager.addRule(overTurnRule);

        double price = 1000d;
        for (int i = 1; i < 500; i++) {
            CandleET candleET = new CandleET(i * interval);
            price -= 1d;
            candleET.setClose(price);
            candleETSeries.add(candleET);
            rulesManager.traverseRules(candleET);
        }

        for (int i = 500; i < 1000; i++) {
            CandleET candleET = new CandleET(i * interval);
            price += 1d;
            candleET.setClose(price);
            candleETSeries.add(candleET);
            rulesManager.traverseRules(candleET);
        }
    }
}
