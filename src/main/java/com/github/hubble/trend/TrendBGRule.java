package com.github.hubble.trend;


import com.github.hubble.rule.IRule;

import java.util.function.Function;


public class TrendBGRule extends IRule {


    protected final TrendEntity trendEntity;

    protected final Function<TrendEntity, Boolean> function;


    public TrendBGRule(String name, TrendEntity entity, Function<TrendEntity, Boolean> fc) {

        super(name);
        this.trendEntity = entity;
        this.function = fc;
    }


    @Override protected boolean match(long id) {

        return this.function.apply(this.trendEntity);
    }
}
