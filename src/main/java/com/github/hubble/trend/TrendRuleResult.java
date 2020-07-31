package com.github.hubble.trend;


import com.github.hubble.common.CandleType;
import com.github.hubble.rule.RuleResult;


public class TrendRuleResult extends RuleResult {


    private TrendManager trendManager;


    public TrendRuleResult(TrendManager trendManager) {

        super(null);
        this.trendManager = trendManager;
    }


    @Override public void call(CandleType candleType, long id) {

        this.trendManager.update(candleType);
    }
}
