package com.github.hubble;


import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.CustomCompare;
import com.github.hubble.ele.NumberET;
import com.github.hubble.indicator.MAIndicatorSeries;
import com.github.hubble.indicator.group.ComparePairIndicatorSeries;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.rule.condition.OverTurnRule;
import com.github.hubble.rule.logic.AndRule;
import com.github.hubble.rule.result.FixedRuleResult;
import com.github.hubble.rule.series.BooleanRule;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public class T {


    public static void main(String[] args) {

        RulesManager rulesManager = new RulesManager();

        long duration = TimeUnit.MINUTES.toSeconds(1);
        Series<CandleET> candleETSeries = new Series<>("A", 128, duration);

        MAIndicatorSeries ma05 = new MAIndicatorSeries("A_ma05", 128, duration, 5);
        MAIndicatorSeries ma10 = new MAIndicatorSeries("A_ma10", 128, duration, 10);
        MAIndicatorSeries ma30 = new MAIndicatorSeries("A_ma30", 128, duration, 30);

        CustomCompare<NumberET> cc = (e1, e2) -> Double.compare(e1.getData(), e2.getData());
        ComparePairIndicatorSeries<CandleET, NumberET> ma05VS10 = new ComparePairIndicatorSeries<>("A_ma05VS10", 128, duration, ma05, ma10, cc);
        ComparePairIndicatorSeries<CandleET, NumberET> ma10VS30 = new ComparePairIndicatorSeries<>("A_ma10VS30", 128, duration, ma10, ma30, cc);

        candleETSeries.bind(ma05VS10).bind(ma10VS30);

        BooleanRule ma05VS10BR = new BooleanRule("ma05VS10BR", ma05VS10);
        BooleanRule ma10VS30BR = new BooleanRule("ma10VS30BR", ma10VS30);
        AndRule andRule = new AndRule("AndRule", ma05VS10BR, ma10VS30BR);
        OverTurnRule overTurnRule = new OverTurnRule("OverTurnRule", andRule, true);
        overTurnRule.setResult(new FixedRuleResult("A出现买入信号"));
        rulesManager.addRule(overTurnRule);

        double price = 1000d;
        for (int i = 1; i < 500; i++) {
            CandleET candleET = new CandleET(i * duration);
            price -= 1d;
            candleET.setClose(price);
            candleETSeries.add(candleET);
            rulesManager.traverseRules();
        }

        for (int i = 500; i < 1000; i++) {
            CandleET candleET = new CandleET(i * duration);
            price += 1d;
            candleET.setClose(price);
            candleETSeries.add(candleET);
            rulesManager.traverseRules();
        }
    }
}
