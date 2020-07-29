package com.github.hubble.rule;


import com.github.hubble.common.CandleType;
import com.github.hubble.trend.TrendManager;


public class TrendRuleResult extends RuleResult {


    private TrendManager trendManager;

    private CandleType candleType;


    public TrendRuleResult(TrendManager trendManager, CandleType candleType) {

        super(null);
        this.trendManager = trendManager;
        this.candleType = candleType;
    }


    @Override public void call(long id) {

        this.trendManager.update(this.candleType);
    }
}
